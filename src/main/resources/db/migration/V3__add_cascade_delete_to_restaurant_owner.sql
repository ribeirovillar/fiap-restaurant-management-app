-- First, drop the existing foreign key constraint
ALTER TABLE restaurants DROP CONSTRAINT IF EXISTS restaurants_owner_id_fkey;

-- Then recreate it with CASCADE deletion
ALTER TABLE restaurants 
ADD CONSTRAINT restaurants_owner_id_fkey 
FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE; 