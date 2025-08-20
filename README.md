# ShoutOut App

ShoutOut is an Android social media app that allows users to create, like, edit, and delete posts in real-time using **Firebase Firestore** and **Firebase Authentication**. The app demonstrates **MVVM architecture**, **Kotlin Coroutines**, **RecyclerView**, and clean Android development practices.

---

## Features

- **Google Sign-In Authentication**  
  Users can sign in with their Google account. User info is stored in Firestore.

- **Create, Edit, Delete Posts**  
  Authenticated users can create new posts, edit their posts, or delete them.

- **Like Posts**  
  Users can like/unlike posts. Likes are updated in real-time.

- **Real-Time Feed**  
  Posts are displayed in a RecyclerView and updated in real-time using Firestore listeners.

- **Toolbar and Floating Action Button**  
  Custom toolbar with logout functionality and a floating button to add new posts.

- **Utility Functions**  
  Human-readable timestamps for post creation (e.g., "5 minutes ago", "yesterday").

---

## Tech Stack

- **Language:** Kotlin, XML
- **Architecture:** MVVM  
- **Firebase:** Authentication, Firestore Realtime Database  
- **UI:** RecyclerView, MaterialToolbar, FloatingActionButton  
- **Asynchronous:** Kotlin Coroutines  
- **Libraries:** Glide (for image loading)

---

## Project Structure

com.example.shoutout
│
├── ui # Activities (Main, SignIn, CreatePost)
├── adapters # RecyclerView Adapter
├── daos # Data Access Objects for Firestore
├── models # Data models (User, Post)
├── viewmodels # MVVM ViewModels
├── utils # Utility functions
└── FirestoreLiveData.kt # LiveData wrapper for Firestore queries

---

## Screenshots

<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/55f91621-36df-4f17-8b2c-5658dfaf94ec" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/e7d262c0-b61b-430c-86f5-3c2e4fdff610" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/12218bff-616a-46fe-b502-6208f3a047a2" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/60b641ce-2731-4e4a-b063-843a4f0c10da" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/99caa1c2-a09a-4110-8110-1cf0d298a0f3" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/1f4c4d7a-07ae-4c10-8dfd-03eb403dae05" />

---

## Getting Started

1. Clone the repository:  
   ```bash
   git clone https://github.com/rahulgos/ShoutOut.git
   ```
2. Open in Android Studio and sync Gradle.

3. Add your Firebase project and google-services.json in app/ folder.

4. Build and run the app on an emulator or physical device.
