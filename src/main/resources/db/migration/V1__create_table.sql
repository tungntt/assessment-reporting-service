CREATE TABLE IF NOT EXISTS project (
    id INT8 PRIMARY KEY,
    name VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS employee (
    id INT8 PRIMARY KEY,
    name VARCHAR(60)
);

CREATE TABLE IF NOT EXISTS time_record (
    id INT8 PRIMARY KEY,
    employee_id INT8 NOT NULL,
    project_id INT8 NOT NULL,
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL,
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id)
);

-- Indexes for optimized query performance
CREATE INDEX idx_time_record_employee_id ON time_record(employee_id);
CREATE INDEX idx_time_record_project_id ON time_record(project_id);
CREATE INDEX idx_time_record_time_from ON time_record(time_from);
CREATE INDEX idx_time_record_composite ON time_record(time_from, employee_id, project_id);

