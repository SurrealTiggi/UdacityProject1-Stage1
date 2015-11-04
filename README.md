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
*1) Implement infinite scroll (Set page, on low scroll fetch page 2, repopulate model. On up scroll, deduct page and fetch to repopulate).
*2) Change OKHttp to use generic AsyncTask
*3) Build trailers/reviews on initial fetch rather than on detailview

# Extras:
* Infinite scrollview
* Extra sorting options
