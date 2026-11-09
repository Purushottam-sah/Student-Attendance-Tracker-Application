# Task List - Profile Photo Implementation

- [ ] Database Upgrade
    - [ ] Update `SQLiteHelper.java`: Increment `DATABASE_VERSION` to 12.
    - [ ] Update `profiles` table schema to include `profileImage` column.
    - [ ] Add `onUpgrade` logic for `profileImage`.
- [ ] Profile Photo Selection
    - [ ] Update `ProfileFragment.java`: Add image picker logic.
    - [ ] Implement image saving (copy to internal storage).
    - [ ] Update database with image path.
- [ ] UI Display
    - [ ] Update `ProfileFragment.java` to load and show the photo.
    - [ ] Update `HomeFragment.java` to show photo in top-right header.
    - [ ] Update `AdminDashboardActivity.java` to show photo in header.
- [ ] Verification
    - [ ] Build success.
    - [ ] Test gallery picker and persistent storage.
