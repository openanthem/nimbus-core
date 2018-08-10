import { Serializable } from './serializable';

export class RedirectHandle implements Serializable<RedirectHandle, string> {
    redirectRoute: string;
    token: string;
    commandUrl: string;

    deserialize( inJson ) {
        this.redirectRoute = inJson.redirectRoute;
        this.token = inJson.token;
        this.commandUrl = inJson.commandUrl;
        return this;
    }
}
