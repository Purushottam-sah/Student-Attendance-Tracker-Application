# Implementation Plan - Premium Bottom Navigation Redesign

Redesign the Bottom Navigation bar in the Student portal to achieve a modern, "Premium" look. The current basic design will be replaced with a sleek, rounded Material 3 style that matches the high-end institutional theme.

## User Review Required

> [!IMPORTANT]
> **Floating Style**: I will transform the bottom navigation into a **floating bar** with rounded corners (24dp) and a subtle shadow. This creates a much more modern "premium app" feel.
>
> **Active Indicator**: I will implement the **Pill-shaped indicator** for the active item, making it very clear which section is currently selected.

## Proposed Changes

### [Phase 1: Bottom Navigation Overhaul]

#### [MODIFY] [activity_main.xml](file:///C:/Users/hp/AndroidStudioProjects/StudentAttendanceTracter/app/src/main/res/layout/activity_main.xml)
- Wrap the `BottomNavigationView` in a `MaterialCardView` to create the floating effect.
- Add margins to the left, right, and bottom.
- Set a high corner radius (24dp).
- Remove the default background of the `BottomNavigationView`.

#### [MODIFY] [themes.xml](file:///C:/Users/hp/AndroidStudioProjects/StudentAttendanceTracter/app/src/main/res/values/themes.xml)
- Add specific attributes for `BottomNavigationView` to use the new **Royal Indigo** palette for the active indicator.
- Disable the default top border/shadow of the system navigation bar to avoid clashing with the floating look.

### [Phase 2: Visual Refinement]

#### [MODIFY] [bottom_nav_menu.xml](file:///C:/Users/hp/AndroidStudioProjects/StudentAttendanceTracter/app/src/main/res/menu/bottom_nav_menu.xml)
- Ensure icons are using the high-fidelity versions and titles are clean.

## Verification Plan

### Manual Verification
1. Open the Student Portal.
2. Verify the Bottom Navigation is **floating** above the screen content.
3. Verify that clicking an item shows a smooth transition and a **pill-shaped indicator**.
4. Check that the corners are rounded and the color matches the Royal Indigo theme.
5. Verify that switching between Home, Subjects, Notes, and Settings works flawlessly with the new design.
