DO language plpgsql
$$
BEGIN
ALTER TABLE tblloan add column returndate timestamp with time zone;
EXCEPTION when others then
RAISE NOTICE  'Column returndate already exists so doesnt need adding';
END;
$$;
