# AndroidHub

AndroidCodelab

Programming Language :
Kotlin

Libraries :
1. Timber
2. Firebase
3. RecyclerView

Overview :
The purpose of the project is to learn and demonstrate various android features and pratices in a single app.

Structure :
1. The app contains a Splashscreen and a HomeScreen .
2. The HomeScreen has a GridLayout recyclerview where each item is used to demonstrate an android feature .
3. It uses Jetpack (anroidx.* libraries) instead of deprecated android.support.* libraries .
4. Multiple product flavours(live,dev,uat) and build variants(release , debug) with each product flavour having it's own applicationId so that all product variants can be installed and tested in a device at the same time.

Following and project naming conventions and guidelines 
https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md


Google Maps:
1. Integrations for live,dev builds
2. Support different terrains .
3. Add markers,point of interests and their info.
4. Map styling (https://mapstyle.withgoogle.com/)
5. Open setting page - Extension function

Firebase ML
1. Facedetection
2. Business Card Detection ( Object detecion + Text detection)

Known issues : Some api's might not work for UAT ( eg. google maps api)