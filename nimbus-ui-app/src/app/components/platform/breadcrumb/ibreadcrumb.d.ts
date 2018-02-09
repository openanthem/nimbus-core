import { Params } from '@angular/router';

interface IBreadcrumb {
    id: string;
    label: string;
    params: Params;
    url: string;
}