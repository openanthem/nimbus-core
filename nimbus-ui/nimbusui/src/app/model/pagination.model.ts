import { Enum } from '../shared/command.enum';

/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

'use strict';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes - Definition for the Pagination object that gets exachanged between server and client.
 *
 * \@howToUse
 *
 */
export interface SortProperty {
  direction: string;
  property: string;
  ignoreCase: boolean;
}

export class SortDirection extends Enum<string> {
  public static readonly ASC = new Enum('asc');
  public static readonly DESC = new Enum('desc');
}

export interface PaginatedRequest {
  pageSize?: number;
  page?: number;
  sort?: Array<SortProperty>;
}

export interface PaginatedPage<T> {
  content?: Array<T>;
  last?: boolean;
  first?: boolean;
  number: number;
  size: number;
  totalPages?: number;
  itemsPerPage?: number;
  sort?: Array<SortProperty>;
}
