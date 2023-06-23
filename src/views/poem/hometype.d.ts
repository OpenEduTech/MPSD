export type Poem = {
	"id": number,
	"title": string,
	"dynasty": string,
	"author": string,
	"content": string,
	"star": number,
	"createdAt": string,
	"updatedAt": string,
	"deletedAt": null
}
export interface PoemList {
	current: number,
	pages: number,
	total: number,
	records: Poem[]
}

export type resData = {
	data: PoemList
}

export type AuthorInfo = {
	description: string;
}

export type SortData = {
	data: { [key: string]: string }
}