# Solution to Fetch Coding Exercise.

## Prompt:

> Please write a native Android app in Kotlin or Java that retrieves the data from https://fetch-hiring.s3.amazonaws.com/hiring.json.
> 
> Display this list of items to the user based on the following requirements:
> 
>     Display all the items grouped by "listId"
>     Sort the results first by "listId" then by "name" when displaying.
>     Filter out any items where "name" is blank or null.
> 
> The final result should be displayed to the user in an easy-to-read list.
> 
> Please make the project buildable on the latest (non-pre release) tools and supporting the current release mobile OS.

## Note:

When sorting by "name," I'm using lexicographic order.

## Building:

1. Clone from VCS with Android Studio
2. Build

although sdk 34 just released, this projects targets 33

I use Android Studio Beta, but I was able to clone the repo into a new project and everything just worked.

## Libraries:

1. Hilt
2. Retrofit with Moshi

## Navigating UI:

Considering the number of items and slight freedom afforded by the prompt, I interpreted 'Display all the items grouped by "listId"' to mean: First just show the "listId" groups, then on click, show all the items with that listId. The listId groups are sorted by their listId. Within the groups, the items are sorted by name and the listId is shown in the Top Bar. Pressing the back arrow in the TopBar or using the system back gesture will bring you back from the items of a listId group to the listId groups.

On init, the view model will try to load the items from server. If it doesn't work, there should be an Error message and a prompt to refresh. Whenever the view model isn't waiting on a network call or processing it, a refresh option is availible to request the data again.

## Testing:

The test should work out of the box. UI Tests integrate with hilt for scalability.

All tests pass with my Pixel 4 running GrapheneOS.

