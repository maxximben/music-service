CREATE TABLE users(
    user_id     SERIAL                 PRIMARY KEY,
    username    VARCHAR(70)            NOT NULL,
    avatar      TEXT,
    password    TEXT                   NOT NULL,
    is_artist   BOOLEAN,
    email       VARCHAR(70)            NOT NULL UNIQUE
);

CREATE TABLE songs(
    song_id     SERIAL                 PRIMARY KEY,
    title       VARCHAR(255),
    user_id     INTEGER
      REFERENCES users(user_id)
          ON DELETE CASCADE,
    album       VARCHAR(255),
    cover       TEXT,
    url         TEXT                    NOT NULL,
    duration    INTEGER
);

CREATE TABLE playlists(
    playlist_id     SERIAL                  PRIMARY KEY,
    title           VARCHAR(255)            NOT NULL,
    user_id         INTEGER
      REFERENCES users(user_id)
          ON DELETE CASCADE,
    cover           TEXT,
    is_album        BOOLEAN,
    is_private      BOOLEAN,
    count_of_songs  INTEGER
);

CREATE TABLE playlist_songs(
    playlist_id     INTEGER     NOT NULL
       REFERENCES playlists(playlist_id)
           ON DELETE CASCADE,
    song_id         INTEGER     NOT NULL
       REFERENCES songs(song_id)
           ON DELETE CASCADE,
    position        INTEGER     NOT NULL,

    PRIMARY KEY (playlist_id, song_id),
    UNIQUE (playlist_id, position)
);

CREATE TABLE users_playlists(
    user_id         INTEGER         NOT NULL
        REFERENCES users(user_id)
            ON DELETE CASCADE,
    playlist_id     INTEGER         NOT NULL
        REFERENCES playlists(playlist_id)
            ON DELETE CASCADE,
    is_owner        BOOLEAN,
    added_at        TIMESTAMP,
    PRIMARY KEY (user_id, playlist_id)
);

CREATE TABLE liked_songs(
    user_id        INTEGER          NOT NULL
        REFERENCES users(user_id)
            ON DELETE CASCADE,
    song_id         INTEGER         NOT NULL
        REFERENCES songs(song_id)
            ON DELETE CASCADE,
    PRIMARY KEY (user_id, song_id)
);

CREATE TABLE similar_songs(
    song_id         INTEGER         NOT NULL
      REFERENCES songs(song_id)
          ON DELETE CASCADE,

    similar_song_id INTEGER
      REFERENCES songs(song_id)
          ON DELETE CASCADE,
    PRIMARY KEY (song_id, similar_song_id)
);

CREATE TABLE analysis_jobs (
    job_id               UUID PRIMARY KEY,
    song_id              INTEGER NOT NULL
       REFERENCES songs(song_id)
           ON DELETE CASCADE,
    status               VARCHAR(30) NOT NULL,
    error                TEXT,
    created_at           TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE song_analysis (
    song_id                INTEGER PRIMARY KEY
       REFERENCES songs(song_id)
           ON DELETE CASCADE,
    audio_length_seconds   DOUBLE PRECISION,
    musicnn                JSONB,
    discogs                JSONB,
    analyzed_at            TIMESTAMP NOT NULL DEFAULT NOW()
);


