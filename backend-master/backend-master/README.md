## Backend Integration
This `README` provides information to integrate front-end with backend. 
There will be minimum changes on HTML files to preserve front-end design. 
Instead, communication logic will be presented as separate JavaScript files. 
Despite the best effort, in the case where an HTML file must be changed, 
a change log will be attached to the bottom of this list.

#### Version
Backend: v0.6.10 TORTELLI
- Added `user/POST`. Not tested.

Known issues
- None.

#### General Instructions
1. Import MySQL database from `showtime_wevent_20201118.sql` file.
2. Open Java project.
3. Add Maven support if needed.
4. Verify database port, username, and password in `src/main/resources/application.properties`.
5. Place front-end files in `src/main/resources/static`.
   (Warning: `src/main/resources/static/js/server` may include server-client communication files.)

For detailed instructions, check instructions for Demo on 
[Confluence](https://201fptesting3.atlassian.net/wiki/spaces/DOC/pages/229779/Demo+Installation+Guide).

#### Version Specific Instructions
- None.