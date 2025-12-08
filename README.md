# Treet: An Interactive Edible Fruit Tree Map

This application provides an interactive map of edible trees throughout Edmonton, allowing users to discover free food resources in their community. The tool allows anyone to locate fruit and nut trees, learn about various species, and explore sustainable foraging opportunities in their neighbourhood. By making this information easily accessible, the app supports urban foraging, environmental education, and community connection with local food sources.

## Description

Our project addresses key questions: Who wants to discover edible trees in their community, and who seeks to learn about and utilize these urban food resources? With these questions in mind, we identified potential users, including nature enthusiasts, urban foragers, educators, and individuals seeking sustainable food sources. To use the app, run the script to launch an interactive map of Edmonton's edible trees. Zoom into your area of interest and click "Refresh Clusters" to update the map and reveal individual trees within that region. Click anywhere on the map to drop an adjustable radius marker that displays detailed information about nearby edible trees. Finally, use the built-in filters to narrow your search by tree type or other characteristics to find exactly what you're looking for.

## Getting Started

### Dependencies

* Maven
* ArcGIS
* JavaFX

### Setup Instructions

Use Java 17 or newer
IntelliJ IDEA
An ArcGIS API key (free)
 
1. Get an API Key or just use mine that's uploaded

* Go to https://location.arcgis.com/ 
* Sign up for free 
* Create an API key
* Copy it

2. Download ArcGIS SDK

* Go to https://developers.arcgis.com/java/install-and-set-up/
* Download the SDK (200.6.0)
* Unzip it
* Copy all files to ~/.arcgis/200.6.0/ on your computer

For Mac: Run this in Terminal after copying:
`
bashsudo xattr -r -d com.apple.quarantine ~/.arcgis
`
3. Open the Project

* Download or clone this project
* Open IntelliJ IDEA
* Click File → Open
* Select the edible-trees-map folder

4. Add Your API Key

* Open EdibleTreesApp.java
* Find line 108
* Replace your_api_key with your actual API key
* Save

5. Run It

* Open the Maven panel on the right side
* Expand Plugins → javafx
* Double-click javafx:run

OR

* Click the green play button

The map should open showing Edmonton

#### Troubleshooting

* If you get "Could not find ArcGIS SDK":

  - Check that the SDK is in ~/.arcgis/200.6.0/
  - Mac users: run the xattr command above

* If you see security warnings on Mac:

  - Just click "Done" on each warning


### Executing program

#### Clustering
Many levels of the map can be shown, from all the way zoomed out to all the way zoomed in. Every time you zoom in and want the clusters to change press 'Refresh Clusters'.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/e7f9f597-8af5-4932-802e-e08509c9a136" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/3e5c9e0d-1bd4-486c-947d-4dcf60841335" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/12e899ed-927f-4611-a69e-4c64a511d1f4" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/cf642913-a10a-4f80-8072-9205571e1e79" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/cd57f4e6-de02-4880-86ae-bb9319415c64" />

#### Filtered Clustering
To filter what fruit types you would like, open the drop down 'Filter by Tree Type' and unselect/select the checkboxes. 

<img width="671" height="614" alt="image" src="https://github.com/user-attachments/assets/9fcb2c49-0ae6-4604-ae87-dd8432d2cb88" />
<br><br>

This will change what fruits are displaying on the map in real-time.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/30f76b3f-0409-4b6a-a029-28991fb15137" />
<br><br>

The clusters also display the filter.

|*with filter*|*without filter*|
|---|---|
|<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2e13784d-2034-4f0d-9939-7e0d745b250c" />|<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/a0992bc3-d7ca-4ab3-b220-bea75dc445ce" />|

<br><br>
#### Individual Trees
Zoom all the way in, press 'Refresh Clusters' and you should be able see each individual tree. At this level a load of features can be accessed

##### Tree Info on Hover
Hover on individual points and a display of that trees info will pop-up.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/8f0be2c3-5cbf-4d68-859f-9e118e66502c" />
<br><br>
##### Dynamic Radius and Pie Chart Fruit Type Distribution
Click anywhere on the map while in the individual trees view and a radius will appear. This radius can be controlled through a slider on the right hand side of the application. A pie chart of the distribution of the fruit types in that radius will appear in the side panel. The pie chart will update in real-time as the radius and distribution changes.

| | |
|---|---|
|<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/9fcb7fb7-1488-4748-afc4-777bbb7017e5" />|<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/b1b84c96-5fec-40ad-8c4f-c6a222efdc71" />|

<br><br>
##### Pie Chart and Filtering
The pie chart will update when different fruit types are selected in the drop down 'Tree Types'.

| | |
|---|---|
|<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/965fe854-b2a1-446a-afd8-e8f7bdb1424d" />|<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/ef7d2d56-f318-49da-9531-1e600f4afdaf" />|

<br><br>

## Authors

Sydney Thiessen<br>
Justin Thai<br>
Kamar El-Hayouni<br>



