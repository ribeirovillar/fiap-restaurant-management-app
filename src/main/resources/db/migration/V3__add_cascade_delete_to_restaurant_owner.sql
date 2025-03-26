ALTER TABLE restaurants DROP CONSTRAINT IF EXISTS restaurants_owner_id_fkey;

ALTER TABLE restaurants
ADD CONSTRAINT restaurants_owner_id_fkey 
FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE; 