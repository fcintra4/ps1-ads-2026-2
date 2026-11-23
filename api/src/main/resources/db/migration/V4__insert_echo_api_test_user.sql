INSERT INTO users (fullname, username, email, password, is_admin)
VALUES (
    'EchoApiTest',
    'echo_test_1234567',
    'echo.test.1234567@example.com',
    '$argon2id$v=19$m=66560,t=3,p=4$yPm0pNsnIa5DxcEVy43Ryw$y+o6VT9R7cZy5bNkSsgnZjQ18IXVEWBwmdI3zVdVIgc',
    FALSE
)
ON CONFLICT DO NOTHING;
