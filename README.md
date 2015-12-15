# UTubeUClient
An Android client for the UtubeU web and websockets server

##Directions for current use
1. Go to http://utubeu.herokuapp.com with Google Chrome browser, login with a gmail account, 
   and create a chatroom.
2. From a command line, run "adb devices", which should list the attached phone or tablet
3. Then from the directory that the apk is in run "adb install -r app-debug.apk" or "adb install -r \<name-of-apk\>"
   if it is named differently
4. Now on the phone or tablet, the app is installed so look in your installed apps and press UtubeU app button
5. Press the Login button, if you don't have Google Play Services installed it should display a Toast.
6. If it toasts an error, go to the Google Play Store and download Google Play Services.
7. Login and have fun with the chatroom.


##Backend and web code
If you want, checkout github.com/boydjohnson/utubeu