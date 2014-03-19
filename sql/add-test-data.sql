-- This SQL file loads sample content to DB.

-- Creates users.
INSERT INTO users(username,
 	          salt,
	          passwd_hash,
                  first_name,
                  last_name,
                  email,
                  description,
                  user_type,
                  created_at,
                  updated_at)
VALUES
('rodde',
 'addksladfddsfkl5',
 'fdkslafjkdsljfkdl53',
 'Rodion',
 'Efremov',
 'rodionef@cs.helsinki.fi',
 'Ima admin.',
 'Admin',
 NOW(),
 NOW());

INSERT INTO users(username,
                  salt,
                  passwd_hash,
                  first_name,
                  last_name,
                  email,
	          show_email,
                  description,
                  user_type,
                  created_at,
		  updated_at)
VALUES
('modde',
 'fdsfd4r453',
 'fdkslfjdksl',
 'Woody',
 'Woodpecker',
 'woody@multilog.com',
 FALSE,
 'Ima mod.',
 'Mod',
 NOW(),
 NOW());

INSERT INTO users(username,
                  salt,
                  passwd_hash,
                  first_name,
                  last_name,
                  email,
	          show_email,
                  description,
                  user_type,
                  created_at,
		  updated_at)
VALUES
('User',
 'fdsf543d4r453',
 'fdkslfjfdsdksl',
 'Pekka',
 'Murkka',
 'murkkis@multilog.com',
 FALSE,
 'Ima user.',
 'User',
 NOW(),
 NOW());

-- Creates topics.



-- Creates threads.



-- Creates posts.
