Use Java 17 or newer
IntelliJ IDEA
An ArcGIS API key (free)

Setup Instructions
1. Get an API Key or just use mine that's uploaded

Go to https://location.arcgis.com/
Sign up for free
Create an API key
Copy it

2. Download ArcGIS SDK

Go to https://developers.arcgis.com/java/install-and-set-up/
Download the SDK (200.6.0)
Unzip it
Copy all files to ~/.arcgis/200.6.0/ on your computer

For Mac: Run this in Terminal after copying:
bashsudo xattr -r -d com.apple.quarantine ~/.arcgis
3. Open the Project

Download or clone this project
Open IntelliJ IDEA
Click File → Open
Select the edible-trees-map folder

4. Add Your API Key

Open EdibleTreesApp.java
Find line 60
Replace your_api_key with your actual API key
Save

5. Run It

Open the Maven panel on the right side
Expand Plugins → javafx
Double-click javafx:run

The map should open showing Edmonton

Troubleshooting
If you get "JavaFX runtime components are missing":

Make sure you're running from Maven (javafx:run), not the green play button

If you get "Could not find ArcGIS SDK":

Check that the SDK is in ~/.arcgis/200.6.0/
Mac users: run the xattr command above

If you see security warnings on Mac:

Just click "Done" on each warning