export interface Pin {
    pinId: number,
    userId?: number,
    locationName: string,
    latitude: number,
    longitude: number,
    isSaved: boolean,
    isDraggable: boolean,
    iconUrl: string
}
