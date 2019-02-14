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

import { LinkPipe } from './link.pipe';

describe('LinkPipe', () => {
    const pipe = new LinkPipe();

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('pipe should return updated anchor tag with http', () => {
      expect(pipe.transform('http://test.com', '/path/test')).toEqual('<a href="http://test.com" target="_blank">http://test.com</a>');
  });

  it('pipe should return updated anchor tag with www', () => {
    expect(pipe.transform('www.test.com', '/path/test')).toEqual('<a href="http://www.test.com" target="_blank">www.test.com</a>');
  });

  it('pipe should return updated anchor tag with mailto: in href attribute', () => {
    expect(pipe.transform('test@gmail.com', '/path/test')).toEqual('<a href="mailto:test@gmail.com">test@gmail.com</a>');
  });

});