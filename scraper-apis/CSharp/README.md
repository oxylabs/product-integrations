This project provides you with Scraper-API example use-cases. Use this code to interact with any other Scraper-APIs by changing the `source` parameter in POST payload data.

You can find Scraper APIs documentation here: https://developers.oxylabs.io

For full access to all available targets, please contact our Sales Manager or your Account Manager.

# Requirements

- .Net Core
- Newtonsoft.Json
- Dotnet script

# Installing dependencies

Install the package for dealing with Json
```
$ dotnet add package Newtonsoft.Json
```

Then, install a tool for running individual CSharp files
```
$ dotnet tool install -g dotnet-script
```

# Running the app

Run the script:

```
dotnet script SingleQuery.cs
```
