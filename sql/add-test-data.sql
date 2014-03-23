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
 'addksladfddsfkl5',
 'fdkslafjkdsljfkdl53',
 'Rodion',
 'Efremov',
 'rodionef@cs.helsinki.fi',
 'Ima admin.',
 'Admin',
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
