ALTER TABLE platform_user ALTER COLUMN email_verified DROP DEFAULT;
ALTER TABLE platform_user ALTER COLUMN email_verified TYPE INT2 USING (CASE WHEN email_verified THEN 1 ELSE 0 END::INT2);