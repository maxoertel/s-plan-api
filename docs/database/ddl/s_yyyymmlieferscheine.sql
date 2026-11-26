ALTER TABLE s_202604lieferscheine
ADD COLUMN entladungwasserkorrektur DOUBLE;

ALTER TABLE s_202606lieferscheine
ADD COLUMN instrackgestartetdatumzeit timestamp;

ALTER TABLE s_202609lieferscheine
ADD COLUMN instrackangenommendatumzeit timestamp;
