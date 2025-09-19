// 단일 쿠키 조회 결과
export interface CookieResponse {
  success: boolean;
  message: string;
  data: CookieDetail;
}

// 쿠키 상세 정보
export interface CookieDetail {
  id: number;
  name: string;
  imageUrl: string;
  health: number;
  ability: string;
  unlockStarCandies: number;
  partner: string | null;
  petId: number;
  petName: string;
  releaseDate: string; // ISO 형식 날짜
  rarity: "COMMON" | "RARE" | "EPIC" | "LEGENDARY" | "SPECIAL";
  type: "BALANCE" | "SPEED" | "CHARGE" | "FLY" | "JUMP";
  description: string;
}

// 쿠키 리스트 조회 결과
export interface CookieListResponse {
  content: CookieDetail[];
  pageable: Pageable;
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
}

// // 쿠키 요약 정보 (리스트 항목 하나)
// export interface CookieSummary {
//   id: number;
//   name: string;
//   health: number;
//   rarity: "COMMON" | "RARE" | "EPIC" | "LEGENDARY" | "SPECIAL";
//   // ... 필요한 필드 추가 가능
//   // 예: imageUrl?: string; type?: string;
// }

// 페이징 관련 정보
export interface Pageable {
  sort: SortInfo;
  offset: number;
  pageSize: number;
  pageNumber: number;
}

// 정렬 정보
export interface SortInfo {
  sorted: boolean;
  unsorted: boolean;
}
