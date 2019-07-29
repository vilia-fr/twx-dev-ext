# ThingWorx development extensions

One day this repository is going to be a sophisticated developer's toolbox. As of today, it provides only trivial functionality
substituting JSON.stringify, which is somewhat broken in ThingWorx:

```javascript
var str = Resources["DevelopFunctions"].JsonToString({ json: yourJsonHere });
```

## Installing

1. Download [extension's ZIP file](https://raw.githubusercontent.com/vilia-fr/twxdevext/master/build/distributions/develop-ext.zip)
2. Make sure that your ThingWorx is configured to allow installing server-side extensions (ThingWorx 8.4+)
3. Open ThingWorx Composer > Import/Export > Extensions > Import
4. Select the ZIP file, upload and refresh the browser page as suggested
5. You can use the extension, no need to restart the server

## Upgrading

To upgrade the extension, you'll need to repeat the same steps as for installing.

## Removing

To remove the extension from your ThingWorx installation you'll need to:

1. Check your code to make sure that you don't use it anywhere (a handy way to do it would be exporting all your entities "to source control",
i.e. as a bunch of XML files and do a `grep` over those files, looking for `DevelopFunctions`.
2. Uninstall it via Composer > Import/Export > Extensions > Manage
3. Restart the server

## Dependencies

The extension is based on [Google gson](https://github.com/google/gson) library, which is also distributed under Apache 2.0 license. 
Gson is not used in out-of-the-box ThingWorx, this library is quite lightweight and there are no other dependencies, 
so it shouldn't conflict with the standard ThingWorx distribution.

## Building

1. Copy JAR files from ThingWorx Extension SDK into /twx-lib directory;
2. Execute `ant -f build-extension.xml`

## Disclaimer

Vilia does not support this extension, nor liable for any side effects of installing it on your ThingWorx system. 
Use it on your own risk. **DO NOT USE IT IN PRODUCTION ENVIRONMNENT!**

