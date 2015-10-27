## Udacity Project 1

This repo contains Project 1 as part of the Udacity Android Developer Nanodegree.

**NOTE:**

*In order for the API calls to work to TMDB, you'll have to create an <b>api.xml</b> file inside <b>app/src/main/res/values/</b> containing your own api key as follows:*

```xml
<resources>
    <string name="API_KEY">YOUR KEY HERE</string>
</resources>
```

# TO DO:
1) Parcelate the movie model
2) Write AsyncTask to fetch trailers and populate movie detail activity
3) Write AsyncTask for reviews
4) Add favorites class (on click = read JSON string from shared pref, reconstruct model, save back in as string)
5) Optimize for tablet
6) Implement all activity lifecycle overrides
7) Implement infinite scroll (Set page, on low scroll fetch page 2, repopulate model. On up scroll, deduct page and fetch to repopulate).

# Extras:
* Infinite scrollview
* Extra sorting options