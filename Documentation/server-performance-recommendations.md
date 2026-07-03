# Tellervo Server PHP Performance Recommendations

This note records performance improvements identified during a PHP webservice
audit. The focus is on changes that should make common user-facing requests
feel faster without changing the API contract.

## Implementation Status

The following recommendations have been implemented:

- Configurable request logging.
- Session-cached administrator status.
- Duplicate login query removal.
- Lazy debug logger loading.
- Server-side dictionary cache/versioning.

The remaining recommendations still need profiling and implementation:

- Batch permission checks in search results.
- Reduce measurement read query count.
- Add short-lived statistics caching.
- Consider front controller autoloading or more selective includes.

## Highest Impact, Lowest Risk

### Make Request Logging Configurable - Implemented

The server can now avoid writing full request payloads during normal request
handling.

Relevant code:

- `src/main/php/config.php.template`
- `src/main/php/inc/request.php`
- `src/main/php/inc/auth.php`

Configuration:

```php
$requestLogMode = "summary";
```

Supported values:

- `off`: disables request logging.
- `summary`: logs request metadata without storing full XML payloads.
- `full`: logs the full XML request payload, matching the older behavior.

Default:

- New configurations default to `summary`.
- Existing configurations without `$requestLogMode` also behave as `summary`.

Expected benefit:

- Reduces database writes on every request.
- Reduces storage growth.
- Avoids storing large XML request bodies unless an administrator explicitly
  enables full logging.

### Cache Session Admin Status - Implemented

The authentication class now stores administrator status in `$_SESSION` after
login and reuses it on later authenticated requests.

Relevant code:

- `src/main/php/inc/auth.php`

The value is refreshed during plain login and secure nonce login. If an older
session does not already contain the cached value, the server refreshes it once
and stores it for later requests.

Expected benefit:

- Avoids an extra database query on most authenticated requests.

### Remove Duplicate Login Queries - Implemented

The login flow now reuses the first user lookup result after password
verification instead of repeating the same query.

Relevant code:

- `src/main/php/inc/auth.php`

This applies to both plain login and secure nonce login.

Expected benefit:

- Makes login quicker and reduces database load during busy teaching or lab
  sessions where many users sign in at once.

## Medium-Sized Improvements

### Add Server-Side Dictionary Cache - Implemented

Dictionary XML is now cached server-side using a version fingerprint derived
from the dictionary source tables.

Relevant code:

- `src/main/php/config.php.template`
- `src/main/php/inc/dictionaries.php`

Configuration:

```php
$dictionaryCacheTTL = 300;
```

Behavior:

- Repeated dictionary requests reuse a cached XML blob when the dictionary
  source table fingerprint still matches.
- The fingerprint includes row counts, PostgreSQL row-version markers, and
  timestamp columns where available.
- Security user/group membership tables are included because those dictionaries
  include group membership.
- `tblsample` is included because the box dictionary can include sample counts.
- Set `$dictionaryCacheTTL = 0;` to disable the cache.

Expected benefit:

- Avoids repeatedly rebuilding the full dictionary XML payload.
- Reduces database queries and PHP object construction during startup.

### Batch Permission Checks In Search Results

Search result handling calls permission checks repeatedly for individual rows
and entity types.

Relevant code:

- `src/main/php/inc/search.php`
- `src/main/php/inc/auth.php`

Recommendation:

- Gather IDs from the search result set and check permissions in batches.
- Where possible, move permission visibility into the search SQL or a database
  helper function.
- Return permission metadata alongside the search result rows.

Expected benefit:

- Reduces query count for searches that return many objects.
- Makes large browse/search operations more predictable.

### Reduce Measurement Read Query Count

Measurement retrieval performs several separate queries for the base
measurement, readings, index state, hierarchy information, and notes.

Relevant code:

- `src/main/php/inc/measurement.php`

Recommendation:

- Review whether `cpgdb.getvmeasurementresult`, `vwcomprehensivevm`, and
  related views can return the commonly needed metadata in one call.
- Avoid per-reading note lookups where a joined or aggregated note result can
  be returned with the readings.
- Profile a large measurement request before and after changing this path.

Expected benefit:

- Reduces latency for opening larger series.
- Reduces round trips between PHP and PostgreSQL.

### Cache Statistics Briefly

The statistics endpoint runs multiple separate count queries.

Relevant code:

- `src/main/php/inc/statistics.php`

Recommendation:

- Combine counts into fewer SQL calls where practical.
- Cache the resulting summary for a short time, such as 1 to 5 minutes.
- Invalidate or bypass the cache for administrative diagnostics if necessary.

Expected benefit:

- Makes dashboard or startup statistics cheaper without risking stale data for
  long periods.

## Structural Improvements

### Lazy Load Debug Logging - Implemented

Debug logging support is now loaded only when `$debugFlag === TRUE`.

Relevant code:

- `src/main/php/inc/errors.php`
- `src/main/php/inc/FirePHPCore/FirePHP.class.php`
- `src/main/php/inc/ChromePhp.php`

When debug output is disabled, `$firebug` is a lightweight no-op logger. This
keeps existing `$firebug->log(...)` calls compatible without loading FirePHP on
normal production requests.

Expected benefit:

- Reduces bootstrap cost on every production request.
- Reduces exposure to compatibility issues in optional debug libraries.

### Reduce Front Controller Includes

The front controller includes many class files up front for every request.

Relevant code:

- `src/main/php/index.php`

Recommendation:

- Introduce a small autoloader for classes in `src/main/php/inc`.
- Alternatively, include files only in the request handlers that need them.
- Keep the initial change conservative because some files may rely on global
  side effects during include.

Expected benefit:

- Reduces request startup time.
- Makes small API calls pay for less unused code.

## Suggested Implementation Order

1. Add configurable request logging. Implemented.
2. Cache administrator status in the session. Implemented.
3. Remove duplicate login queries. Implemented.
4. Lazy load debug logging. Implemented.
5. Add server-side dictionary cache/versioning. Implemented.
6. Batch search permission checks.
7. Reduce measurement read query count.
8. Add short-lived statistics caching.
9. Consider autoloading after the smaller changes have landed.

The search and measurement changes should be developed with profiling data and
a representative database because they touch higher-value workflows and more
SQL.
