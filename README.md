# Takeawayapp
Job application assignment for takeaway

## Assignment - Summary
The goal of this test is to implement a sample project, where you visualize a
restaurant list. You are able to sort the restaurant list based on it’s current openings state,
you can favourite a restaurant and you can select a sort value to further sort the list. Finally
we would like to see you add the option to filter the restaurant list, based on a restaurant’s
name. In the attachments you can find a JSON file (sample.json), this file contains all the
necessary data to complete this assignment. Parse the JSON file and use it for the
visualization and sorting of the list. Use the following priority of the sorting (from the
highest to the lowest priority):

1. Favourites : Favourite restaurants are at the top of the list, your current favourite
restaurants are stored locally on the phone.
2. Openings state : Restaurant is either open (top), you can order ahead (middle) or a
restaurant is currently closed (bottom). (Values available in sample.json)
3. Sort options : Always one sort option is chosen and this can be best match, newest,
rating average, distance, popularity, average product price, delivery costs or the
minimum cost. (Values available in sample.json)
4. Filtering : It’s up to you how you how you want to search by restaurant name.
- Please visualize the name of the restaurants, the current opening state, the selected
sort, the sort value for a restaurant and if it’s a favourite or not.
- Remember if you have multiple favourite restaurants, they are also sorted based on
their current openings state and current selected sort.
- We expect valid test cases
- Readme file with all the needed information, how to get the sample project working
and verify the test cases.

## Design

The architecture is based on the mvvm architecture. Most of the design decisions are based on similar examples by Google (docs, sample apps like sunflower..) and the official android docs (https://developer.android.com/jetpack/guide)

Upon starting the application a worker will load in the example restaurant data. In the main screen, there are several filter chips to sort between the different types of metrics (best_match, average price..) and search based on the name of the restaurant. Additionally, a user can favorite a restaurant in which case it'll appear on top based on the defined sorting priority of the assignment. 

In the viewmodel, a mediatorlivedata is used to observe two other livedatas (RoomQuery livedata or a change in metric). In the fragment, the mediatorLiveData is observed for changes to refresh the ListAdapter.

If no items can be loaded a button with a reset will appear instead which changes the query to trigger a new livedata to be retrieved.

## Setup

This application was tested & debugged using Android Studio 4.2 Canary build

To install it, clone this repository locally and import the project in android studio.
Following that, use the android studio emulator or a local device to build & run the application

## Known issues
- There is an issue with loading data for the first time when running the application. To alleviate this, click on the reset button that will appear in the center of the recyclerview area. After spending quite a long time on debugging this issue, I opted to let it be for now since I couldn't quite figure out why it didn't automatically detect the new entries in the database. Externally, I used the database inspector to trigger a change in the database which seemed to work fine.

## Testing
I've added a limited set of test to the application which checks the worker and the sorting used.
