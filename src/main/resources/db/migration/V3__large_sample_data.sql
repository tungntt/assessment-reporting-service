INSERT INTO employee (id, name)
SELECT
    seq AS id,
    'Employee ' || seq AS name
FROM generate_series(100, 1000) AS seq
    ON CONFLICT (id) DO NOTHING;

INSERT INTO project (id, name)
SELECT seq, 'Project ' || seq
FROM generate_series(1, 200) AS seq
    ON CONFLICT (id) DO NOTHING;

INSERT INTO time_record (id, employee_id, project_id, time_from, time_to)
SELECT
    seq AS id,
    100 + (seq % 100) AS employee_id,
    1 + (seq % 200) AS project_id,
    ts,
    ts + (INTERVAL '1 hour' + random() * INTERVAL '8 hours')
FROM generate_series(1, 5000) AS seq,
LATERAL (SELECT NOW() - (random() * INTERVAL '60 days') AS ts) t
    ON CONFLICT (id) DO NOTHING;
