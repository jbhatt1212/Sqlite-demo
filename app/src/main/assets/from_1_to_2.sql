ALTER TABLE product_table ADD COLUMN
val db = writableDatabase

                                    -- db.beginTransaction()
                                    -- try {
                                    --     // Step 1: Create a new table without the column
                                    --     db.execSQL("""
                                    --         CREATE TABLE users_temp (
                                    --             id INTEGER PRIMARY KEY,
                                    --             name TEXT
                                    --         )
                                    --     """)

                                    --     // Step 2: Copy data to the new table
                                    --     db.execSQL("""
                                    --         INSERT INTO users_temp (id, name)
                                    --         SELECT id, name FROM users
                                    --     """)

                                    --     // Step 3: Drop the old table
                                    --     db.execSQL("DROP TABLE users")

                                    --     // Step 4: Rename the new table
                                    --     db.execSQL("ALTER TABLE users_temp RENAME TO users")

                                    --     db.setTransactionSuccessful()
                                    -- } catch (e: Exception) {
                                    --     e.printStackTrace()
                                    -- } finally {
                                    --     db.endTransaction()
                                    -- }
