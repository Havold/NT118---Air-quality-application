# Air Quality Check Application ☀️ ⛈️ 🌕
## Introduction
The Air Quality Check Application is developed using Java language on Android Studio IDE. This application provides features for weather forecasting, displaying weather information visually on a map, and analyzing recent weather data through charts. All data is fetched from a single API link.
### Note: As the API link is currently inactive, some features may not function properly.
## Features
### Login
- The application provides a login feature for users. (Registration and password change features are currently under development).
### Weather Tab ⛅
- Displays information such as temperature, rainfall, humidity, and wind speed.
- Provides information about the current weather conditions (rainy, sunny, moonlit).
- Includes a button to send notifications to users to suggest appropriate solutions for the weather conditions.
- Automatically sends notifications every 5 minutes to remind users and saves data to an SQL database for statistics.
### Map Tab 🗺️
- Displays a map for users to view weather forecast information at different locations.
- When users tap on a location on the map, it shows the weather information at that location.
### Possible Issue: This tab may encounter errors when running. Simply navigate to res/layout/fragment_map.xml and adjust the <fragment> tag to <androidx.fragment.app.FragmentContainerView>, and vice versa. If you encounter errors with <androidx.fragment.app.FragmentContainerView>, switch back to <fragment>.
### Chart Tab 📊
- Allows users to select attributes and time intervals to display column charts of those attributes' statistics.
## Language 
- Supports 2 languages: __Vietnamese__ and **English** .
## System Requirements
- Android Studio Giraffe | 2022.3.1 Patch 2 (Recommend).
- JDK: 1.8.0. (Recommend).__
- SDK: 34 (Recommend).
- Internet connection.
## How to run
1. Clone the project.
2. Open and import the project to your Android Studio.
3. Click run. ✔️
## Feature Under Development
- Registration and password change features.
## Demo
Please refer to [this demo clip](https://uithcm-my.sharepoint.com/:v:/g/personal/21521479_ms_uit_edu_vn/EabcOxP5r4lOuGsgw9Ls2IoBkqHn9wrBtaYEKMPgUyiStw?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=rjcpHl) for a visual representation of the application.
