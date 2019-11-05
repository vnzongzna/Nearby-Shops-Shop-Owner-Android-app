Nearby Shops 
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Nearby%20Shops%20Open%20Source%20Food%20Delivery%20and%20Hyperlocal%20app&url=https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app&via=moraysumeet&hashtags=opensource,androiddev,fooddelivery,android,ecommerce)
=============

> Self-Hosted Mobile First Hyperlocal and Food Delivery Platform	

<img src="https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app/blob/master/media/nearby-shops-logo-small.png" width="140">    <a href="https://play.google.com/store/apps/details?id=org.nearbyshops.shopkeeperappnew"><img class="alignnone" src="https://goldtonemusicgroup.com/img/goldtone/main-page/news/playstore-badge.png" alt="Get it on Google Play" width="120" height="40" /></a> 



Sell Nearby for Free ? 
-----------------

Nearby Shops is worlds first free and Open-Source mobile E-Commerce Platform.
Anyone can get listed as seller and sell (deliver orders) for free  without any need to pay any commission.

Setup your shop in Just 30 Minutes and Start Selling

Step 1 : Download Shop Owner app from Download link given above

Step 2 : Select a Market of your choice and Register your shop

There could be many local markets in your area. Just select anyone which you feel appropriate. You need to register seperately for each market. After selecting the market please Sign-Up create your new account from the Login Screen. 

Step 3 : Update your Shop Profile and Add Photos

Add a name, description and add Photos for your Shop. 

Step 4 : Add Items to Shop

In the Shop Dashboard you will see Items Database. Add Items to your shop that you want to sell. 

Step 5 : Set Prices and Availibility

After you have added items to the shop. Go to quick stock editor and set prices and availibility for each item. When you set the prices and availibity your shop will be visible in the customer app. 



Your Shop is Ready to Accept Orders from customers. To check whether your shop is visible and able to receive orders please download the End-User app and check if your shop is visible. Send a fake order and see if you get any push notification.

If you are facing any difficulty feel free to ask for help and support at our forum https://forum.nearbyshops.org


Tips for Troubleshooting

Your shop will not be visible until and unless you have set prices for the items you added into your shop. So please check that you have set the prices and availibility. 



How it works ? 
-----------------

Nearby Shops is a free of cost nonprofit e-commerce platform for local vendors. Anyone can get listed as seller and deliver orders for free without any need to pay any comission. It is run and managed by volunteers and cooperatives. And the technology is fully open-source and free of cost. 

It is a first of its kind democratic, non-commercial, Open-Source and Nonprofit E-commerce platform. We aim to provide world class free of cost e-commerce platform to the sellers. 

Technically Nearby Shops is a distributed ecosystem of local markets. This project is conceptually similar to [Mastadon project](https://github.com/tootsuite/mastodon). Anyone can self-host their Local Market Instance and create their local market on Nearby Shops. The technology is fully open-source and funded by Donations. We dont charge any fee or comission in order to use the platform.

Local Market's are generally self-hosted by independent Volunteers, Service Providers and local vendor cooperatives who seek the welfare of their local vendors.



Volunteer and Help us
-------------------------------------------------

Our goal is to provide an e-commerce platform which is technologically advanced, free of cost, open, democratic and Non-Commercial. 

If you like this Idea and this project Please volunteer and Help us ! 
You can help us in various ways even if you do not know how to write code !

1. Host an instance of Local market where other sellers could register. You will work as the administrator of your local market.
2. Help us to Promote this project in your local community !


This is an International Platform with support for multiple curriencies. Please help us making e-commerce truly free of cost for everyone. 

To participate please contact us on support@nearbyshops.org or simply post your thoughts on our forum 
https://forum.nearbyshops.org




About Nearby Shops
--------------------

Nearby Shops is an Open-Source food delivery, grocery and hyperlocal app platform. You can install your self-hosted instance on Digital Ocean or AWS and get your grocery or food delivery market up and running for just $ 5 per month in hosting fees.


Nearby Shops implements Alibaba's Grocery Online-to-Offline Concept. Customers can place and order from the app and pick it up in the store. Home Delivery is also available !

The installation guide and app customization guide is provided at http://developer.nearbyshops.org


Website :  https://nearbyshops.org | Developers Guide: http://developer.nearbyshops.org





<img src="https://raw.githubusercontent.com/SumeetMoray/Nearby-Shops-End-User-Android-app/master/media/items_reduced.gif" width="208">   <img src="https://raw.githubusercontent.com/SumeetMoray/Nearby-Shops-End-User-Android-app/master/media/order_detail_reduced.gif" width="208">   <img src="https://raw.githubusercontent.com/SumeetMoray/Nearby-Shops-End-User-Android-app/master/media/shop_home_reduced.gif" width="208"> 



## 🚩 Table of Contents
- [Concept](#concept)
- [Tech Stack and App Architecture](#tech-stack-and-app-architecture)
- [Features and highlights](#features-and-highlights)
- [Screenshots](#screenshots)
- [Libraries Used](#libraries-used)
- [Third Party Integrations](#third-party-integrations)
- [Community forum](#community---need-help-)
- [Source Code for Shop Owner and Admin app and REST API](#source-code-for-shop-owner-admin-app-and-server-side)
- [Contributions Required](#contributions-welcome)
- [Contributor Compensation Program](#contributor-compensation-program)
- [Development Instructions](#development-instructions)
- [License](#license)


Concept
----------
Nearby Shops is a Hyperlocal Shopping platform where a customer can buy directly from the shops available in his/her local area and get their orders delivered to their home or just pick the order up from the shop. 

In the new emerging world, customers are getting tired of going outside the home to shop every day. They would much prefer to simply place an order and have the products delivered to their home the same day. 

Conventional e-commerce has issues ... the delivery takes a long time and it's more difficult to trust unknown vendors whom you cannot see and meet. Hyperlocal e-commerce solves these issues because delivery is faster and customers can reach out to the vendors easily. 

Nearby Shops can also be used as a food Delivery platform where restaurants can use it to deliver food. 

Nearby Shops implements Alibaba's New Offline-to-Online Concept, where customers discover products online and then pick those products up from physical stores. 

![Online to Offline](https://www.innovationiseverywhere.com/wp-content/uploads/2015/05/what-is-o2o-online-to-offline-digital-ecommerce-retail-china-gmic-1-2.jpg)





Tech Stack and app Architecture
--------------------------------

Uses Android Jetpack and Google’s Recommended Architecture Patterns - Migration to MVVM is planned

Written in both Java and Kotlin : Migration to kotlin is planned and will be coming in near future

Built using Butterknife, Retrofit, Ok-HTTP, Picasso, Dagger, and the Mapbox Android SDK

User-Interface is made using the modular View-Holder Pattern which makes the UI blocks modular, easy to reuse, modify and understand.

The app uses Single Activity Architecture, where most of the screens are implemented as fragments and there are only 2-3 activities in the entire project.

The project has 3 Android apps. One app each for the end-user, shop-owner and adminstrator. There is also a JSON-based REST API on the backend. The source-code for the REST API is provided. 



Features and Highlights
----------------------------

Support for Multiple Currencies : You can set your locale and country and currency for your local market will be set accordingly.

Mobile First - Multi-Vendor Platform where customers can send orders to multiple Shops / Restaurants

Integrations for Sending SMS-OTP, E-mail and Push Notifications are available

Order Tracking, with live status updates for customers using E-mail, SMS and Push Notifications

Rating and Reviews for items and shops

Home delivery and pick-up from the shop (Online-to-Offline) Shopping Concept is Supported

Location based filtering is available, which means that customers will see only those shops which can deliver to their address -- other shops will be filtered out. 

Vendors / Shop-Owners can track orders through orders inventory and update the order progress !

By adding delivery staff, shop-owners can deliver orders by themselves. Shared delivery logistics will also be available in future releases of Nearby Shops. 

Billing and payments are supported. Shop-owners are billed according to the number of orders they deliver successfully. And Service Providers (Market Owners) can collect payments from the shop-owners. 

App Customization Permitted - You can whitelabel or customize the android app with your own branding using this customization guide. https://developer.nearbyshops.org/installation/customize-apps.html

Nearby Shops Multi-Market Mode - you can add your local market to nearby shops market discovery service and your market becomes visible and accessible to Global audience of Nearby Shops app. Read more about Nearby Shops multi-market mode !

Commercial Use requires license. 



Screenshots
-----------------

<img src="https://nearbyshops.org/images/3.png" width="208">   <img src="https://nearbyshops.org/images/5.png" width="208">   <img src="https://nearbyshops.org/images/6.png" width="208">




Libraries Used
---------------

Android-Jetpack, Retrofit, Ok-Http, Event-Bus, Picasso, butterknife, dagger2, U-Crop, Gesture Views, Smiley Rating, Mapbox Android SDK


Third Party Integrations
-------------------------
SMS-OTP using MSG91 (More Integrations coming soon ...)

E-mail using Simple-Java-Mail E-mail Library

Push Notifications using Firebase and One-Signal

Maps using Mapbox Android SDK and Google Maps

Payment Gateway (Coming Soon ...)


Community - Need Help ? 
------------------------

If you want any help regarding anything. Feel free to contact us -- send a message through our forum or you can simply post an issue. Your issues will not be ignored and you will surely receive help. 

Reach out to us - Please Visit - https://forum.nearbyshops.org



Source code for Shop-Owner, Admin app and Server Side
-------------------------------------------------------
If you want to see source code for Shop-Owner app, Admin-app and server side the links are provided below

End-User app : https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app

Admin app : https://github.com/SumeetMoray/Nearby-Shops-Admin-App

Source code for Server Side JSON Based REST API is available in this repository https://github.com/SumeetMoray/Nearby-Shops-API


Contributions Welcome
-----------------------------------------------

https://forum.nearbyshops.org/t/project-roadmap-and-contributions-required/34/4

Improved Search with Search Suggestions for android app

Develop a PWA and Web app for Nearby Shops

i18n Internationalization support

Payment Gateway Integrations

Performance Testing and Optimization

Suggestions and Improvements in App Architecture

Code Review and Architecture



Contributor Compensation Program
------------------------------------

We believe developer's must be compensated for the work they do. 

How many times you have made a contribution to a project you love but havent received any compensation for that. Unfortunately unpaid working hours is the sad reality for many open-source developers ...

We want to change this ...Therefore we have launched contributor compensation scheme !
When you make a contribution to this project you will be paid at the rate of 10-15 $ per hour we can make it upto 50 $ per hour for contributors from countries where cost of living is high. 

Please note that we reserve the right to accept or reject your contribution. You will be paid only if your contribution is accepted. 

Ability to pay for contributions depend on availibilty of funds ... we recommend you to [contact us and talk with us](https://forum.nearbyshops.org/t/contributor-compensation-program/93) before you plan to make any contribution to this project.



Development Instructions
-------------------------

If you're a developer looking to work on the source code follow these instructions to start developing !

```js
git clone https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app.git
```

You should now open the project with the latest version of Android Studio.



License
=======

    Copyright 2017-19 Nearby Shops Open-Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

