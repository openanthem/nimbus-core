/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Subscriber } from 'rxjs';

// creating the decorator DestroySubscribers
export function DestroySubscribers() {
  return function (target: any) {
    // decorating the function ngOnDestroy
    target.prototype.ngOnDestroy = ngOnDestroyDecorator(target.prototype.ngOnDestroy);
    // decorator function
    function ngOnDestroyDecorator(f) {
      return function () {
        // saving the result of ngOnDestroy performance to the variable superData 
        let superData = f ? f.apply(this, arguments) : null;
        // unsubscribing
        for (let subscriberKey in this.subscribers) {
          let subscriber = this.subscribers[subscriberKey];
          if (subscriber instanceof Subscriber) {
            subscriber.unsubscribe();
            console.log('unscribed',subscriber);
          }
        }
        // returning the result of ngOnDestroy performance
        return superData;
      };
    }
    // returning the decorated class
    return target;
  };
}
