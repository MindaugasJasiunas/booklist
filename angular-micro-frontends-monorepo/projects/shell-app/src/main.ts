import { enableProdMode } from '@angular/core';
import { environment } from './environments/environment';

import('./bootstrap')
	.catch(err => console.error(err));

// remove console.logs when going into production
if (environment.production) {
    // redefine console.log to a function that does nothing
    window.console.log = () => {};
    enableProdMode();
}
