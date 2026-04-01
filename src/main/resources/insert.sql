-- 1. Пользователи
INSERT INTO users (username, avatar, password, is_artist, email) VALUES
    (' moonlightkiller',   'https://i.pravatar.cc/150?u=1',  '$2a$12$YS8as6ETJashdEj6Ph4r/e5zmD/lc0RTkUb6EQbel/YMElSRB2O8G', true,  'moon@artist.ru'),
    (' summer_vibes',     'https://i.pravatar.cc/150?u=2',  '$2a$12$YS8as6ETJashdEj6Ph4r/e5zmD/lc0RTkUb6EQbel/YMElSRB2O8G', false, 'anya summer@gmail.com'),
    (' dj_darkside',      'https://i.pravatar.cc/150?u=3',  '$2a$12$YS8as6ETJashdEj6Ph4r/e5zmD/lc0RTkUb6EQbel/YMElSRB2O8G', true,  'darkside@beats.pro'),
    (' just_listening',   NULL,                             '$2a$12$YS8as6ETJashdEj6Ph4r/e5zmD/lc0RTkUb6EQbel/YMElSRB2O8G', false, 'listener94@mail.ru'),
    (' indie_heart',      'https://i.pravatar.cc/150?u=5',  '$2a$12$YS8as6ETJashdEj6Ph4r/e5zmD/lc0RTkUb6EQbel/YMElSRB2O8G', false, 'indie.soul@yandex.ru');


-- 2. Треки (привязываем к артистам — user_id 1 и 3)
INSERT INTO songs (title, user_id, album, cover, url, duration) VALUES
    ('Neon Rain',         1, 'Midnight City',    'https://picsum.photos/seed/neonrain/300/300',    'https://s3.twcstorage.ru/dff2fb2a-f4c1-4ba0-a0ef-42aab0ae6870/audio/1770212025622_Never%20Gonna%20Give%20You%20Up%20-%20Rick%20Astley.mp3',     218),
    ('Broken Orbit',      1, 'Midnight City',    'https://picsum.photos/seed/neonrain/300/300',    'https://s3.twcstorage.ru/dff2fb2a-f4c1-4ba0-a0ef-42aab0ae6870/audio/1770212025622_Never%20Gonna%20Give%20You%20Up%20-%20Rick%20Astley.mp3',   187),
    ('Afterimage',        1, NULL,                'https://picsum.photos/seed/afterimage/300/300',  'https://s3.twcstorage.ru/dff2fb2a-f4c1-4ba0-a0ef-42aab0ae6870/audio/1770212025622_Never%20Gonna%20Give%20You%20Up%20-%20Rick%20Astley.mp3',       245),
    ('Echoes in Dust',    3, 'Black Geometry',   'https://picsum.photos/seed/blackgeo/300/300',    'https://s3.twcstorage.ru/dff2fb2a-f4c1-4ba0-a0ef-42aab0ae6870/audio/1770212025622_Never%20Gonna%20Give%20You%20Up%20-%20Rick%20Astley.mp3',      304),
    ('Voidwalker',        3, 'Black Geometry',   'https://picsum.photos/seed/blackgeo/300/300',    'https://s3.twcstorage.ru/dff2fb2a-f4c1-4ba0-a0ef-42aab0ae6870/audio/1770212025622_Never%20Gonna%20Give%20You%20Up%20-%20Rick%20Astley.mp3',       276),
    ('Fading Horizon',    3, NULL,                NULL,                                              'https://s3.twcstorage.ru/dff2fb2a-f4c1-4ba0-a0ef-42aab0ae6870/audio/1770212025622_Never%20Gonna%20Give%20You%20Up%20-%20Rick%20Astley.mp3',   195);


-- 3. Плейлисты
INSERT INTO playlists (title, user_id, cover, is_album, is_private, count_of_songs) VALUES
    ('Моя ночь 2025',           1, 'https://picsum.photos/seed/playlist1/300/300', false, false, 0),
    ('Deep Drive',              2, 'https://picsum.photos/seed/deepdrive/300/300',  false, false, 0),
    ('Only 2025 Hits',          4, NULL,                                         false, true,  0),
    ('Black Geometry — Full',   3, 'https://picsum.photos/seed/blackgeo/300/300',  true,  false, 0),
    ('Chill & Rainy Days',      5, 'https://picsum.photos/seed/rainychill/300/300', false, false, 0);


-- 4. Связь пользователей и плейлистов (владельцы + добавленные в избранное)
INSERT INTO users_playlists (user_id, playlist_id, is_owner, added_at) VALUES
   (1, 1, true,  '2025-11-10 14:30:00'),
   (2, 2, true,  '2025-12-03 09:15:00'),
   (4, 3, true,  '2026-01-08 22:40:00'),
   (3, 4, true,  '2025-10-29 18:22:00'),
   (5, 5, true,  '2026-02-14 13:05:00'),

-- другие пользователи добавили себе чужие плейлисты
    (2, 1, false, '2025-11-15 10:10:00'),
    (5, 1, false, '2025-11-20 17:45:00'),
    (1, 5, false, '2026-02-20 08:30:00'),
    (4, 2, false, '2025-12-10 23:12:00');


-- 5. Песни в плейлистах
INSERT INTO playlist_songs (playlist_id, song_id, position) VALUES
-- Моя ночь 2025 (user 1)
(1, 1, 1),   -- Neon Rain
(1, 2, 2),   -- Broken Orbit
(1, 3, 3),   -- Afterimage

-- Deep Drive (user 2)
(2, 4, 1),   -- Echoes in Dust
(2, 5, 2),   -- Voidwalker
(2, 6, 3),   -- Fading Horizon
(2, 1, 4),   -- Neon Rain

-- Black Geometry — Full (альбом)
(4, 4, 1),
(4, 5, 2),

-- Chill & Rainy Days
(5, 3, 1),
(5, 6, 2),
(5, 2, 3);


-- 6. Лайкнутые треки
INSERT INTO liked_songs (user_id, song_id) VALUES
   (1, 4), (1, 5),           -- moonlightkiller лайкнул треки dj_darkside
   (2, 1), (2, 2), (2, 3),   -- summer_vibes лайкнула все треки moonlightkiller
   (4, 1), (4, 5),
   (5, 3), (5, 6), (5, 4);


