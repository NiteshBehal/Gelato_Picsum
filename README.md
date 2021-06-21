Gelato Assignment

Language Used: Kotlin

Architectural Pattern: MVVM + Repository Pattern Feature Coverage:

• All mandatory features

• Request Cache

• Image Cache

• Download Image

• Share Image




Package Structure:

After opening the application package (com.gelato.picsum), we see the following structure:

• ‘data’ folder: Responsible for providing data to the UI Layer. Currently data is being fetched from remote when network is available and saved to DB which acts as single source of truth.
It contains Repository, Database , DAO, DB Entities and api to fetch ImageData. ImageData api functionality, along with load more, is implemented using Android Paging3 Library.
The data layer is reactive. It uses Flows and LiveData to hold its state, to which the UI layer listens via ViewModel and updates itself accordingly.

• ‘di’ folder: Contains dependency injection implementation using Dagger 2. Currently, it contains application level dependencies and fragment level dependencies.

• ‘ui’ folder: The UI layer of the application. Contains Activity, Fragment, ViewModel, Adapters and ViewHolders etc. The ViewModel interacts with the repository implementation of data layer to fetch the data.

• ‘utils’ folder: Contains some helper classes and constants.

• ‘GelatoPicsumApplication’ file: Application file.




TechStack Used:

• Android Jetpack Navigation Component: To navigate between fragments.

• ViewModels: To fetch data from repository layer and provide it to ui layer.

• Coroutines: To handle threading.

• LiveData and Flows: To keep persistent and upto date data.

• Paging3 Library: To implement load more functionality for infinite scroll.

• Retrofit: To handle api calls.

• Room DB: To implement offline cache of data.

• Glide: To load and cache images from URL.

• Dagger2: To implement dependency injection.

• DataBinding: To bind data directly with xml views
