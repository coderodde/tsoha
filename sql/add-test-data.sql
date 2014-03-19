INSERT INTO users(username,
                  first_name,
                  last_name,
                  email,
                  description,
                  user_type,
                  created_at,
                  updated_at)
VALUES
('rodde',
 'Rodion',
 'Efremov',
 'rodionef@cs.helsinki.fi',
 'Ima admin.',
 'Admin',
 NOW(),
 NOW());

INSERT INTO users(username,
                  first_name,
                  last_name,
                  email)
