DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED ALWAYS AS IDENTITY,
    user_email VARCHAR(255) UNIQUE NOT NULL,
    user_username VARCHAR(32) UNIQUE NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_is_manager BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS reimbursement_requests (
    reimbursement_request_id INTEGER GENERATED ALWAYS AS IDENTITY,
    reimbursement_request_employee_id INTEGER NOT NULL,
    reimbursement_request_amount FLOAT NOT NULL CHECK (reimbursement_request_amount > 0.0),
    reimbursement_request_type VARCHAR(24) NOT NULL,
    reimbursement_request_pending BOOLEAN NOT NULL,
    reimbursement_request_approved BOOLEAN NOT NULL,
    reimbursement_request_manager_id INTEGER,
    PRIMARY KEY (reimbursement_request_id),
    FOREIGN KEY (reimbursement_request_employee_id) REFERENCES users (user_id),
    FOREIGN KEY (reimbursement_request_manager_id) REFERENCES users (user_id)
);

INSERT INTO users (user_email, user_username, user_password, user_is_manager)
VALUES ('thewallacems@gmail.com', 'jacob', 'password', 'TRUE') 
ON CONFLICT DO NOTHING;
