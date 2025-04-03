
---

# 📷 Unsplash Image Gallery App  

A Jetpack Compose Android app that fetches images from **Unsplash API**, displays them in a **Lazy Grid**, and allows users to **view, download, and favorite** images.  

## 🚀 Features  
✅ Fetches images from **Unsplash API**  
✅ Displays images in a **LazyVerticalGrid**  
✅ **Detail Screen** with full-screen image & photographer details  
✅ **Download images** (handles runtime permissions)  
✅ **Favorite images** using **Room Database**  

---

## 🛠️ Tech Stack  
- **Kotlin** (Jetpack Compose)  
- **Retrofit** (for API calls)  
- **Coil** (for image loading)  
- **Room Database** (for storing favorites)  
- **Hilt** (for dependency injection)  

---

## 📦 Setup & Installation  

### 1️⃣ Clone the Repository  
- Download the project by cloning the GitHub repository.  

### 2️⃣ Get Unsplash API Key  
- Register on **Unsplash Developers** and get an **API Key**.  
- Add it to the project’s local configuration.  

### 3️⃣ Configure Unsplash API in Retrofit  
- Set up Retrofit to fetch images using the **Unsplash API**.  
- Define the API endpoints and pass the API key in requests.  

### 4️⃣ Display Images in Home Screen  
- Implement a **LazyVerticalGrid** to show images.  
- Each image includes the **photographer’s name** and a click event to open the **Detail Screen**.  

### 5️⃣ Implement Detail Screen  
- Show the **full-screen image** along with photographer details.  
- Provide buttons for **downloading** and **favoriting** the image.  

### 6️⃣ Download Images (Handling Permissions)  
- Use **runtime permissions** to handle **storage access**.  
- Implement **DownloadManager** to save images to the device.  

### 7️⃣ Favorite Images Using Room Database  
- Create a **Room Database** to store favorite images.  
- Implement functions to **add**, **remove**, and **list** favorites.  
- Use **ViewModel and LiveData** to update UI in real time.  

### 8️⃣ Implement Dependency Injection with Hilt  
- Set up **Hilt** for dependency injection.  
- Inject dependencies into ViewModel, Repository, and Database.  

---

## 📷 Screenshots  
| Home Screen  | Detail Screen | Favorites |  
|--------------|--------------|------------|  
| ![Home](https://github.com/user-attachments/assets/e92bf466-e3b0-419d-81c9-1e7c5ab155a3) | ![Detail](https://github.com/user-attachments/assets/bc7c56e1-2fa3-42ea-b04b-db9f065c621d) | ![Favorites](https://github.com/user-attachments/assets/54e344ef-382a-402e-9984-d00c86456086) |  

---

## 🚀 Future Enhancements  
- 🔍 Add search functionality  
- 🌙 Implement dark mode  
- 📌 Support image categories  



