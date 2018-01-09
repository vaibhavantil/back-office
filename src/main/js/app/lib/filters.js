export const filterList = (filter, list) => (
    list.filter((item) => item.state === filter)
)