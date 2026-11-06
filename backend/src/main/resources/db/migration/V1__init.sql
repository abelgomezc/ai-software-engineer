CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE task_run (
    id BIGSERIAL PRIMARY KEY,
    repo_source VARCHAR(1024) NOT NULL,
    task_description TEXT NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    finished_at TIMESTAMP,
    iterations INT NOT NULL DEFAULT 0,
    tokens_in INT NOT NULL DEFAULT 0,
    tokens_out INT NOT NULL DEFAULT 0,
    final_diff TEXT,
    summary TEXT,
    branch_name VARCHAR(255)
);

CREATE TABLE run_step (
    id BIGSERIAL PRIMARY KEY,
    task_run_id BIGINT NOT NULL REFERENCES task_run(id) ON DELETE CASCADE,
    step_number INT NOT NULL,
    role VARCHAR(64),
    thought_summary TEXT,
    tool_name VARCHAR(128),
    tool_input JSONB,
    tool_output TEXT,
    duration_ms BIGINT,
    status VARCHAR(32),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE code_chunk (
    id BIGSERIAL PRIMARY KEY,
    task_run_id BIGINT REFERENCES task_run(id) ON DELETE CASCADE,
    file_path VARCHAR(1024),
    start_line INT,
    end_line INT,
    content TEXT,
    embedding VECTOR(768)
);

CREATE TABLE eval_suite (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE eval_task (
    id BIGSERIAL PRIMARY KEY,
    suite_id BIGINT NOT NULL REFERENCES eval_suite(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    repo_path VARCHAR(1024),
    task_description TEXT NOT NULL,
    difficulty VARCHAR(32)
);

CREATE TABLE eval_result (
    id BIGSERIAL PRIMARY KEY,
    suite_id BIGINT NOT NULL REFERENCES eval_suite(id) ON DELETE CASCADE,
    eval_task_id BIGINT NOT NULL REFERENCES eval_task(id) ON DELETE CASCADE,
    task_run_id BIGINT REFERENCES task_run(id),
    passed BOOLEAN,
    iterations INT,
    duration_ms BIGINT,
    tokens_in INT,
    tokens_out INT,
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
