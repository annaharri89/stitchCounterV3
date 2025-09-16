# Stitch Counter V3 - TODO List

## 🎨 UI/UX Improvements

### Theme & Visual Design
- [ ] Create a color scheme that the user can change
- [ ] Get light and dark mode working
- [ ] Create new app icon

### Counter Interface
- [ ] Rework the UI for the counters so it's more intuitive
- [ ] Make it so the double counter UI will never overflow the screen
- [ ] Make it so the UI is responsive whether the user is in portrait or landscape mode

### Library Interface
- [ ] Rework the library UI, make it a grid list and make images show up
- [ ] Allow users to upload pictures of their projects progress

## ⚙️ Settings & Configuration
- [ ] Finish the settings screen

## 💾 Data Management & Backup
- [ ] Allow the user to import export csv files so they can back up projects since it's a local database

## 🔗 External Integrations
- [ ] See if you can integrate with ravelry.com in anyway (sdk?)

---

## 📋 Notes

### Priority Levels
- **High**: Core functionality and user experience improvements
- **Medium**: Data management and external integrations
- **Low**: Nice-to-have features

### Implementation Order Suggestion
1. Theme system (light/dark mode + color schemes)
2. Counter UI improvements and responsiveness
3. Library UI rework with image support
4. Settings screen completion
5. Data import/export functionality
6. Ravelry integration research
7. App icon design

### Technical Considerations
- Consider using Jetpack Compose's Material 3 theming system
- Implement proper state management for theme changes
- Use ConstraintLayout or similar for responsive design
- Consider using Room database for local storage
- Research Ravelry API documentation for integration possibilities

---

*Last updated: $(date)*