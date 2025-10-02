export interface PetDetail {
  name: string;
  imageUrl: string;
  ability: string;
  rarity: "COMMON" | "RARE" | "EPIC" | "LEGENDARY" | "SPECIAL";
  releaseDate: string;
  description: string;
}
