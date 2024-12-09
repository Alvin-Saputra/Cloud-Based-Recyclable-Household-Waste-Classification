# **Waste Classification App**  
An Android-based application that classifies waste types using Machine Learning, provides detailed information about waste, and displays nearby waste banks on an interactive map.

---

## **Table of Contents**  
1. [Project Description](#project-description)  
2. [Features](#features)  
3. [Technologies Used](#technologies-used)  
4. [Installation and Usage](#installation-and-usage)  
5. [Application Architecture](#application-architecture)  
6. [Screenshots](#screenshots)  
7. [How It Works](#how-it-works)  
8. [Contributing](#contributing)  
9. [Contact](#contact)  

---

## **Project Description**  
The **Waste Classification App** is an interactive and user-friendly mobile application that helps users classify waste types by scanning images via camera or gallery. It also provides details like waste descriptions, methods, outcomes, and benefits. Additionally, the app features a **Map** to detect the user's current location and find nearby waste banks within a specific radius.  

---

## Features
1. Image-Based Waste Classification
Users can capture an image using the camera or select one from the gallery.
The app processes the image using Machine Learning and displays the classification score (confidence level).
2. Save Classification Results
Users can save classification results for future reference, including the image and score.
3. Detailed Waste Information
Provides information on the classified waste, including:
Description
Method of handling or processing
Outcome after processing
Benefits of proper disposal or recycling
4. Nearby Waste Banks Map
Detects the user's current location using GPS.
Displays waste banks within a specific radius on an interactive Google Map.
Helps users find the nearest facilities for proper waste disposal.

________________________________________

## **Technologies Used**  
### **Languages & Tools**  
- **Programming Language**: Kotlin  
- **Machine Learning Model**: TensorFlow Lite  
- **Database**: Room Database  
- **Location Services**: Google Maps SDK  
- **Development Tools**: Android Studio  

### **Libraries**  
- Retrofit: For API calls and networking  
- Glide: For image loading and caching  
- Google Maps SDK: For map and location integration  
- Lifecycle & ViewModel: For MVVM architecture  
- TensorFlow Lite: For waste classification processing  

## **Installation and Usage**  

### **Prerequisites**  
- Android Studio (latest version)  
- Android device running **Android 5.0 (Lollipop)** or higher  

### **Steps to Install**  

1. **Clone the repository**:  
   ```bash
   git clone https://github.com/Alvin-Saputra/Cloud-Based-Recyclable-Household-Waste-Classification.git
   cd Cloud-Based-Recyclable-Household-Waste-Classification-app

### **Open the Project in Android Studio**
1. Launch **Android Studio**.  
2. Open the project directory by selecting:  
   - `File` > `Open...` > Navigate to your project folder.  

### **Set Up Dependencies**
1. Ensure your `build.gradle` files are **synced**.  
   - Click **"Sync Now"** in the top-right corner if prompted.  
2. Verify that all required libraries are downloaded.  

### **Configure API Keys**
1. Open the `local.properties` file located in your project root directory.  
2. Add your **Google Maps API key**:  

   ```properties
   MAPS_API_KEY=your_google_maps_api_key

### **Run the Application**
1. **Connect your Android device via USB**:
   - Ensure that **Developer Options** and **USB Debugging** are enabled on your device:
     - Go to **Settings** > **About phone** > Tap **Build number** 7 times to enable Developer Options.
     - Go to **Settings** > **Developer Options** > Enable **USB Debugging**.
   
   Alternatively, you can use an **Android emulator** if you do not have a physical device available.

2. **Click the Run button (▶)** in Android Studio:
   - This will build the app and deploy it to your connected device or emulator.

---

### **Grant Required Permissions**
When running the app for the first time, you will need to grant the following permissions:

1. **Camera**:  
   - Allow access to your device's camera so that the app can capture images of waste.

2. **Gallery**:  
   - Allow the app to access your photo gallery so you can select an image of waste from your device.

3. **Location**:  
   - Grant the app permission to access your current location for locating nearby waste banks on the map.
   
These permissions are required for the proper functioning of the app, especially the image capturing and location features.

## **Application Architecture**

This app follows the **MVVM (Model-View-ViewModel)** architecture pattern to ensure a clear separation of concerns and improve code maintainability. Below is an overview of the architecture:

### **1. Model**
- The **Model** is responsible for handling the data and business logic of the app. 
- It includes data classes, repositories, and any network or database interactions.
- For example, in this app, the model handles the classification results, waste details, and waste bank data.
- It interacts with external services like **TensorFlow Lite** for waste classification and **Google Maps API** for locating nearby waste banks.

### **2. View**
- The **View** consists of UI elements and activities or fragments in Android that display the data to the user.
- The View listens to user actions (like button clicks) and displays appropriate information (like classification results, waste types, and map locations).
- It communicates with the ViewModel to fetch or update data.

### **3. ViewModel**
- The **ViewModel** is responsible for holding and managing the UI-related data. It communicates with the **Model** layer to fetch data and provides it to the **View**.
- It helps to decouple the UI from the data layer and ensures that UI data survives configuration changes, such as screen rotations.
- It also handles any business logic related to presenting data to the UI, like transforming raw classification data into readable formats.

### **4. Repository**
- The **Repository** acts as an intermediary between the data sources (API, database, or local storage) and the **ViewModel**.
- It abstracts the source of the data and provides a clean API for data access.
- For instance, the repository may use Retrofit to fetch data from the server or Room Database to store and retrieve past classification results.

### **5. Dependency Injection (DI)**
- The app uses **Dependency Injection** (DI) to provide the necessary dependencies to different parts of the app, such as the ViewModel, Repository, and API service.
- **Hilt** or **Dagger** (or another DI framework) can be used to manage the dependencies and inject them where needed.

### **6. Navigation**
- The app uses the **Navigation Component** for handling the flow between different screens (Activities and Fragments).
- It simplifies the process of navigating between screens while maintaining a consistent back stack.

### **7. Other Components**
- **LiveData**: The app uses **LiveData** to observe changes in the data and automatically update the UI when data changes.
- **Room Database**: For storing classification history, which is displayed in a separate section of the app.
- **Google Maps API**: For displaying the user's current location and finding nearby waste banks.

### **Overall Flow**
1. The **View** (UI) sends user interactions to the **ViewModel**.
2. The **ViewModel** calls the **Repository** to fetch or update data.
3. The **Repository** interacts with the **Model** (API, Database, or external sources).
4. The **ViewModel** then provides the data to the **View** for display, ensuring UI consistency.

This architecture helps separate concerns, making the app easier to maintain, test, and extend.

## **How It Works**

The app works in five main steps:

1. **Capture or Select an Image**:  
   Users can either take a photo of waste using the camera or select an image from the gallery.

2. **Classify the Waste**:  
   Once the image is selected, the user presses the **"Classify Now"** button, which triggers the waste classification process.

3. **View the Results**:  
   After classification, the app shows the classification result, including a score (confidence level), which indicates how accurate the classification is.

4. **Save the Results**:  
   The user can save the classification result by pressing the **"Save"** button. This allows users to store their results for future reference, including the waste type, score, and other details.

5. **View Detailed Information & Nearby Waste Banks**:  
Additionally, the app displays nearby waste banks on the map based on the user's current location or a set radius, allowing users to easily locate the nearest waste bank for proper disposal.

---

## **Contributing**

We welcome contributions to improve this project! If you'd like to contribute, please follow these steps:

### **How to Contribute**

1. **Fork the repository**:
   - Go to the project repository on GitHub and click on the **Fork** button to create your own copy of the repository.

2. **Clone your fork**:
   - Clone the forked repository to your local machine by running:
     ```bash
     git clone https://github.com/your-username/your-repository.git
     ```
   - Navigate to the project directory:
     ```bash
     cd your-repository
     ```

3. **Create a new branch**:
   - Create a new branch for your changes. Name the branch according to the feature or fix you're working on:
     ```bash
     git checkout -b feature-branch
     ```

4. **Make your changes**:
   - Make the necessary changes or additions in the codebase.

5. **Commit your changes**:
   - Commit your changes with a clear, concise message describing the update:
     ```bash
     git commit -m "Add new feature or fix bug"
     ```

6. **Push your changes**:
   - Push the changes to your forked repository:
     ```bash
     git push origin feature-branch
     ```

7. **Create a pull request (PR)**:
   - Go to the **Pull Requests** section of the repository on GitHub.
   - Click on **New Pull Request**.
   - Select your fork and the branch you want to merge into the main repository.
   - Provide a description of your changes and submit the pull request.

### **Contribution Guidelines**

- **Code Style**: Follow the existing code style used in the project for consistency.
- **Issue Tracking**: If you're fixing a bug or implementing a new feature, please check if an issue already exists for it. If not, feel free to create one before starting work.
- **Testing**: Ensure that any changes you make are thoroughly tested and that all tests pass.
- **Documentation**: Update the documentation if necessary, especially if you introduce new features or modify existing ones.

### **Reporting Issues**

If you find any issues or bugs, please follow these steps to report them:

1. Check if the issue already exists by searching through the existing issues.
2. If it doesn't exist, create a new issue providing a clear description of the bug or feature request, including steps to reproduce, expected behavior, and any relevant screenshots or error messages.

### **Code of Conduct**

By contributing to this project, you agree to adhere to our code of conduct. Please be respectful and considerate to all contributors.

Thank you for contributing to this project!

---

## **Contact**

If you have any questions, suggestions, or would like to get in touch regarding this project, feel free to contact me:

**Name**: Alvin Saputra
- **Email**: [a195b4ky0432@bangkit.academy](mailto:a195b4ky0432@bangkit.academy)
- **GitHub**: [https://github.com/Alvin-Saputra/](https://github.com/Alvin-Saputra/)
- **LinkedIn**: [https://www.linkedin.com/in/alvin--saputra/](https://www.linkedin.com/in/alvin--saputra/)

**Name**: Albert Hansel
- **Email**: [a195b4ky0318@bangkit.academy](mailto:a195b4ky0318@bangkit.academy)
- **GitHub**: [https://github.com/AlbertHansel](https://github.com/AlbertHansel)
- **LinkedIn**: [https://www.linkedin.com/in/alberthansel/](https://www.linkedin.com/in/alberthansel/)

I am open to any feedback and collaboration!
