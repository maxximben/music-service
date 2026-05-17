CREATE TABLE IF NOT EXISTS song_authors(
    song_id      INTEGER     NOT NULL
        REFERENCES songs(song_id)
            ON DELETE CASCADE,
    user_id      INTEGER     NOT NULL
        REFERENCES users(user_id)
            ON DELETE CASCADE,
    author_order INTEGER     NOT NULL DEFAULT 1,

    PRIMARY KEY (song_id, user_id),
    UNIQUE (song_id, author_order)
);

CREATE INDEX IF NOT EXISTS idx_song_authors_user_id ON song_authors(user_id);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'songs'
          AND column_name = 'user_id'
    ) THEN
        INSERT INTO song_authors(song_id, user_id, author_order)
        SELECT song_id, user_id, 1
        FROM songs
        WHERE user_id IS NOT NULL
        ON CONFLICT (song_id, user_id) DO NOTHING;

        ALTER TABLE songs DROP COLUMN user_id;
    END IF;
END $$;
