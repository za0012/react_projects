export interface Article {
  id: number;
  title: string;
  content: string;
  user_id: number;
  createdAt: string;
  updatedAt: string;
  viewCount: number;
}

interface Sort {
  // 한 번 쓰는 것이 아니라 여러 번 사용할 것 같아, 따로 빼서 interface로 정의
  empty: boolean;
  unsorted: boolean;
  sorted: boolean;
}

export interface ArticleListResponse {
  content: Article[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: Sort;
    offset: number;
    unpaged: boolean;
  };
  size: number;
  sort: Sort;
  totalElements: number;
  totalPages: number;
}

// type ApiResponse = {
//   content: Article[];
//   // add other properties if needed
// };
