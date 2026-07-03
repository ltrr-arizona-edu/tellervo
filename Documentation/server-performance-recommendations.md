# Tellervo Server PHP Performance Recommendations

This note records performance improvements identified during a PHP webservice
audit. The focus is on changes that should make common user-facing requests
feel faster without changing the API contract.

## Highest Impact, Lowest Risk

### Make Request Logging Configurable

The server currently writes full request payloads synchronously during normal
request handling.

Relevant code:

- `src/main/php/inc/request.php`
- `src/main/php/inc/auth.php`

In `request.php`, `logRequest()` inserts the full XML request and waits for
the result before request processing can continue. Authentication requests are
logged similarly in `auth.php`.

Recommendation:

- Add a server configuration setting for request logging, for example
  `off`, `summary`, or `full`.
- Default production installs to `summary` or `off`.
- Keep `full` logging available for debugging installations and support cases.
- In summary mode, store only timestamp, user, IP address, request type, and
  result status rather than full XML payloads.

Expected benefit:

- Reduces database writes on every request.
- Reduces storage growth.
- Removes a synchronous wait from the request path.

### Cache Session Admin Status

The authentication class checks whether a logged-in user is an administrator
while constructing the session object.

Relevant code:

- `src/main/php/inc/auth.php`

Recommendation:

- Store the administrator flag in `$_SESSION` after login.
- Reuse the session value on subsequent requests.
- Refresh it when the user logs in again, logs out, or when permissions are
  changed by an administrator.

Expected benefit:

- Avoids an extra database query on most authenticated requests.

### Remove Duplicate Login Queries

The login flow currently performs a user lookup, then performs a second lookup
for the same user after password verification.

Relevant code:

- `src/main/php/inc/auth.php`

Recommendation:

- Reuse the row fetched by the first login query.
- Only issue a second query if additional data is genuinely required and cannot
  be returned in the first result.

Expected benefit:

- Makes login quicker and reduces database load during busy teaching or lab
  sessions where many users sign in at once.

## Medium-Sized Improvements

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

### Lazy Load Debug Logging

Debug logging support is loaded during normal request bootstrap, even when
debug logging is disabled.

Relevant code:

- `src/main/php/inc/errors.php`
- `src/main/php/inc/FirePHPCore/FirePHP.class.php`
- `src/main/php/inc/ChromePhp.php`

Recommendation:

- Load FirePHP and ChromePhp only when debug output is enabled.
- Use a tiny no-op logger object when debug output is disabled.

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

1. Add configurable request logging.
2. Cache administrator status in the session.
3. Remove duplicate login queries.
4. Lazy load debug logging.
5. Batch search permission checks.
6. Reduce measurement read query count.
7. Add short-lived statistics caching.
8. Consider autoloading after the smaller changes have landed.

The first four items are good candidates for small, isolated commits. The
search and measurement changes should be developed with profiling data and a
representative database because they touch higher-value workflows and more SQL.
