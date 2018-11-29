/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ParamUtils } from './param-utils';

/**
 * \@author Swetha Vemuri
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
describe('ParamUtils', () => {

    beforeEach(function() {
    });

it('resolveParamUri() method test relative param', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '../postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/tile/section/postButton');
});

it('resolveParamUri() method test with multiple relative params', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '../../../postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/postButton');
});

it('resolveParamUri() method test absolute path', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '/currentdomain/page/tile/section/postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/tile/section/postButton');
});

it('resolveParamUri() method test starts with absolute path and uses relative notation - TODO', function() {
    // tslint:disable-next-line:max-line-length
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '/currentdomain/page/tile/section/grid/../postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/tile/section/grid/../postButton');
});

it('resolveParamUri() method test start with absolute separator - TODO Invalid path', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '/../postButton');
    expect(resolvedPath).toEqual('/../postButton');
});

it('resolveParamUri() method test with <!#this!>', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '<!#this!>/../postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/tile/section/postButton');
});

it('resolveParamUri() method test with .d', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '.d/postButton');
    expect(resolvedPath).toEqual('/currentdomain/postButton');
});

it('resolveParamUri() method test with relative new path', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '../../../tile2/section2/grid2/postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/tile2/section2/grid2/postButton');
});

it('resolveParamUri() method test with cross domain', function() {
    // tslint:disable-next-line:max-line-length
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '/p/anotherdomain/page2/tile2/section2/grid2/postButton');
    expect(resolvedPath).toEqual(null);
});

it('resolveParamUri() method test with another domain', function() {
    // tslint:disable-next-line:max-line-length
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '/anotherdomain/_process?fn=_handler&source=type');
    expect(resolvedPath).toEqual('/anotherdomain/_process?fn=_handler&source=type');
});

it('resolveParamUri() method test single param - Negative Scenario', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', 'postButton');
    expect(resolvedPath).toEqual('/currentdomain/page/tile/section/grid/postButton');
});

it('resolveParamUri() method test empty param - TODO Negative Scenario', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '');
    expect(resolvedPath).toEqual(null);
});

it('resolveParamUri() method test null param - Negative Scenario', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', null);
    expect(resolvedPath).toEqual(null);
});

it('resolveParamUri() method test with params - TODO', function() {
    const resolvedPath = ParamUtils.resolveParamUri('/currentdomain/page/tile/section/grid', '/postButton');
    expect(resolvedPath).toEqual('/postButton');
});

});