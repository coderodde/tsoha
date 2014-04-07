-- This SQL file loads sample content to DB.

-- Creates users.
INSERT INTO users(user_id,
                  username,
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
(1,
 'rodde',
 'abcdefghhgfedcbaabcdefghhgfedcba',
 'f13dc67158d24876c62ff4dd3694c138a12f0e34f0999b01eb8f8764ab8f00c0',
 'Rodion',
 'Efremov',
 'rodionef@cs.helsinki.fi',
 'Ima admin.',
 'ADMIN',
 NOW(),
 NOW());

INSERT INTO users(user_id,
                  username,
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
(2,
 'modde',
 '12345678876543211234567887654321',
 '9b36bda2fd29b636fec62ab3804f4b6690ce5af90e1f15b56748ba3011912564',
 'Woody',
 'Woodpecker',
 'woody@multilog.com',
 FALSE,
 'Ima mod.',
 'MOD',
 NOW(),
 NOW());

INSERT INTO users(user_id,
                  username,
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
(3,
 'User',
 '0123456789abcdef0123456780abcdef',
 'e4cb9300dcc7a4e8c5db2dc2dbc8d84b79c5fd8214940affb56d69f4efa5204e',
 'Pekka',
 'Murkka',
 'murkkis@multilog.com',
 FALSE,
 'Ima user.',
 'USER',
 NOW(),
 NOW());

INSERT INTO users(user_id,
                  username,
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
(4,
 'MC_Admin',
 'abcdef01234567890123456789abcdef',
 '508c034de43bf77e535af1e2b18d4624541309ba8525a0961edaea3cb71d5e00',
 'George',
 'Phunkeeh',
 'phunkeeh@gaymail.com',
 'Hello, glad to be an admin.',
 'ADMIN',
 NOW(),
 NOW());

-- Creates topics.
INSERT INTO topics (topic_id,
                    topic_name,
                    created_at,
                    updated_at)
VALUES
(1,
 'Mathematics',
 NOW(),
 NOW());


INSERT INTO topics (topic_id,
                    topic_name,
                    created_at,
                    updated_at)
VALUES
(2,
 'Computer science',
 NOW(),
 NOW());

-- Creates threads.
INSERT INTO threads (thread_id,
                     topic_id,
	                 thread_name,
                     created_at,
                     updated_at)
VALUES
(1,
 1,
 'Analytic geometry',
 NOW(),
 NOW());

INSERT INTO threads (thread_id,
                     topic_id,
	                 thread_name,
                     created_at,
                     updated_at)
VALUES
(2,
 1,
 'Vector calculus',
 NOW(),
 NOW());

INSERT INTO threads (thread_id,
                     topic_id,
	                 thread_name,
                     created_at,
                     updated_at)
VALUES
(3,
 2,
 'Data structures',
 NOW(),
 NOW());

-- Creates posts.
INSERT INTO posts (post_id,
                   thread_id,
                   user_id,
                   post_text,
                   created_at,
                   updated_at)
VALUES
(1,
 1,
 2,
 'General parabola formula is funny. :-)',
 NOW(),
 NOW());

INSERT INTO posts (post_id,
                   thread_id,
                   user_id,
                   post_text,
                   created_at,
                   updated_at)
VALUES
(2,
 2,
 3,
 'I can compute a flux through a surface in vector fields!',
 NOW(),
 NOW());

INSERT INTO posts (post_id,
                   thread_id,
                   user_id,
                   post_text,
                   created_at,
                   updated_at)
VALUES
(3,
 2,
 1,
 'Keep it up! :0)',
 NOW(),
 NOW());

INSERT INTO posts (post_id,
                   thread_id,
                   user_id,
                   post_text,
                   created_at,
                   updated_at)
VALUES
(4,
 3,
 1,
 'Here it is lonely.',
 NOW(),
 NOW());
