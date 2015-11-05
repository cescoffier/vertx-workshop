# Map Render Verticle

This is a simple demo of a verticle that consumes messages addressed to "map-render" on a REST api and generates a PNG
tile according to the slippy map spec (e.g.: google maps, openstreepmap, etc...)

In order to speedup the startup a index of a area to render should be created before hand. A index is just a serialized
version of a OSM data extract. see [wiki](http://wiki.openstreetmap.org/wiki/Downloading_data).


## Openshift

```
rhc create-app map0render0service https://raw.githubusercontent.com/vert-x3/vertx-openshift-cartridge/master/metadata/manifest.yml
```

**Deployment:**

```
cd openshift
git add -A
git commit -m "deploy my application"
git push
```
