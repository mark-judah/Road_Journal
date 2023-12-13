Road Journal
Road Journal is a mobile app designed to capture and share incidents, experiences, and observations encountered on the road. Whether it's noting accidents,
traffic congestion, beautiful scenery, or anything noteworthy, users can post text or image-based updates to share with the community. The app uses firebase to store the data.

Features
Post Incidents: Create posts with text or images to share road incidents or experiences.
View Incidents: Browse through a feed to discover updates from other users.
Interact: Like, comment, and share interesting posts.

Installation and Usage
Requirements
Android Studio
Gradle
Firebase Account
Firebase Setup
Create a Firebase project at Firebase Console.
Set up Firebase Authentication for the project:
Enable Email/Password or any preferred authentication method.
Set up Firebase Realtime Database or Firestore for storing posts:
Configure database rules as per your security requirements.
Add the Firebase configuration file (google-services.json) to the app directory of your Android project

Build and Run
Clone the repository:


git clone https://github.com/mark-jk/RoadJournal.git
Open the project in Android Studio.

Build the project using Gradle.

Run the app on an Android emulator or a physical device.

Usage
Sign up or log in to start using the app.
Create a new post by selecting the incident type and adding text or an image.
View posts from other users in the feed.
Interact with posts by liking, commenting, or sharing.
