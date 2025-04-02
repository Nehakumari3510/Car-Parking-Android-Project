# Complete Guide to Setup Car Parking Android App

## Getting Started

This guide will walk you through the steps to set up Android Studio and run this project on your machine.

## 1. Steps to Setup Android Studio

 a. Navigate to the official website for downloading the Android Studio 
 
 https://developer.android.com/studio

 b. Then click on download icon it will download **.tar.gz file**.
        

 c. Now extract the archive -
    
   Open your terminal and navigate to Downloads folder.

    cd Downloads

    tar -zxvf android-studio-*.tar.gz 

   (replace **android-studio-*.tar.gz** with the actual filename)

 d. Move the extracted folder to more permanent place (optional but its a good practice to do).

    sudo mv android-studio /opt/

 e. Run the Android Studio Startup Script:

    cd /opt/android-studio/bin
    ./studio.sh 

 f. Check if it installed or not.

    android-studio

 g. Create a Desktop Entry (Optional but Recommended for Easy Launching)

    mkdir -p ~/.local/share/applications/
nano ~/.local/share/applications/android-studio.desktop

Paste the following content into the file (adjust the Path and Exec lines if you didn't move the folder to /opt):

    [Desktop Entry]
    Name=Android Studio
    Comment=The official IDE for Android development
    Exec=/opt/android-studio/bin/studio.sh
    Icon=/opt/android-studio/bin/studio.png
    Type=Application
    Terminal=false
    Categories=Development;IDE;
    StartupWMClass=jetbrains-studio

   Save the file (Ctrl+O, then Enter) and exit the editor (Ctrl+X).

   You might need to run the following command to update the desktop entry database:

    sudo update-desktop-database ~/.local/share/applications/
**Important Considerations -**

 a. **Java Development Kit (JDK):** Android Studio requires a JDK. When you run Android Studio for the first time, it will likely prompt you to download and install one if it's not already present on your system. You can also install a JDK manually using -
  
    sudo apt update
    sudo apt install openjdk-17-jdk  # Or a later version

 b. **System Requirements:** Make sure your Ubuntu system meets the minimum system requirements for Android Studio.

## 2. Steps to Install Emulator Inside Android Studio

 a. Click on the icon at top right corner name device manager.

 b. Click on plus icon there and select **create virtual device**.

 c. Select any device definition then click on next and select system image then click on next.

 d. Give any name to your emulator.

 e. Click on finish or install.

 f. You will find the emulator when click on device manager at the top-right corner. 

## 3. Steps to Create a Basic Project 

 a. Click on three horizontal bars at top left corner.

 b. Navigate to new then click on new project then select the empty view activity.

 c. Click on next and give the name of your project and can give the name to your base package it's optional.

 d. You can select the location where you want to create the project and select the language from Kotlin and Java and click on finish.

 e. then you project build will start and after finshing of that you can click on run icon appearing at top middle.

 f. You will get an actvity with hello world.

## 4. Steps to Clone the Car Parking Project 
 
 a.  **Clone the Repository:**
    Open your terminal or command prompt and navigate to the directory where you want to store the project. Then, run the following command:
    
    git clone <repository_url>
    
   Replace `<repository_url>` with the URL of this repository.

b.  **Open the Project in Android Studio:**
    * Launch Android Studio.
    * In the welcome screen, select **"Open an existing project"**.
    * Navigate to the directory where you cloned the repository and select the root project folder. Click **"Open"**.

c.  **Wait for Gradle Sync:**
    * Android Studio will automatically start Gradle sync. This process downloads the necessary dependencies and configures the project.
    * **Important:** This might take some time depending on your internet connection and project size. Pay attention to the "Gradle Sync" status in the bottom right corner of Android Studio. Ensure it completes without errors.

d.  **Build the Project:**
    * Once Gradle sync is successful, go to **"Build > Make Project"** in the Android Studio menu.
    * This will compile your project code and resources. Check the "Build" output window for any errors.

e.  **Run the Application:**

## 5. Steps to Get the Google Map API Key

* Create the google cloud account.
    
    https://console.cloud.google.com/

* Create a new project in Google Cloud Console.

* Enable the api **Android Maps for SDK** 

* Navigate to credentials tab and get the api key.

* Copy the Google API key and add it inside strings.xml file in our project.

## 6. Steps to Setup the Database

#### 1. Install the PostgreSQL

   a. Before installing any software, update your package list.

    sudo apt update && sudo apt upgrade -y

   b. Install PostgreSQL and the necessary dependencies.
    
    sudo apt install postgresql postgresql-contrib -y

   c. Start and enable the PostgreSQL service.

    sudo systemctl start postgresql
    sudo systemctl enable postgresql

   d. Verify that PostgreSQL is running.

    sudo systemctl status postgresql

### 2. Configure The PostgreSQL

   a. Switch to the PostgreSQL user.

    sudo -i -u postgres

   b. Access the PostgreSQL shell.

    psql
 
   c. Set a password for the postgres user.

    ALTER USER postgres PASSWORD 'your_password';

   d. Exit the PostgreSQL shell.
    
    \q
    exit
 
### 3. Installing the GUI Tool pgAdmin for PostgreSQL

   a. Import the pgAdmin repository.

    curl https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo tee /etc/apt/trusted.gpg.d/pgadmin.asc

   b. Add the repository.

    sudo sh -c 'echo "deb https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list'

   c. Update the Package.
    
    sudo apt update

   d. Install pgAdmin (for both desktop and web modes).

    sudo apt install pgadmin4 -y

### 4. Configure And Access pgAdmin

   a. Run the initial setup.

    sudo /usr/pgadmin4/bin/setup-web.sh

   b. Open pgAdmin in a browser.

    http://localhost/pgadmin4

   c. Login using the email and password set in Step 5
 
### 5. Connect to a New Server Connection

   a. In the left panel, right-click on Servers and select Create → Server...

   b. In the General tab -

   * Enter a name for the connection (e.g., MyDatabaseServer).

   c. In the Connection tab, enter the following details - 

   * Host name/address: localhost (or your database server IP)

   * Port: 5432 (default PostgreSQL port)

   * Maintenance database: your_database_name

   * Username: postgres (or your database user)

   * Password: Enter the password you set for the   PostgreSQL user.

   * Check the Save password option if desired. 

   d. Click **Save** to create the connection.

### 6. Access a Specific Database

   a. Expand the server you just added.

   b. Expand the Databases section.

   c. Click on the database you want to connect to (e.g., mydatabase).

   d. Open the Query Tool by clicking on the SQL icon (⚡).

   e. Run queries on your selected database.

**Now we have done with all the necessary steps now we can suceessfully run our car parking app in android studio.**
