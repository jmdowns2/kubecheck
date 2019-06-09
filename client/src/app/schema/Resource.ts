
interface Resource {
    name: string;
    namespace: string;
    type: string;

    children: Resource[];
}
