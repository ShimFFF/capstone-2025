import { client } from "@apis/client";
import { ResultResponseData } from "@type/api/common";
import { deleteProvidedfileByProvidedFileIdRequest } from "./type";


export const deleteProvidedfileByProvidedFileId = async ({
    providedFileId,
    reason,
  }: Readonly<deleteProvidedfileByProvidedFileIdRequest>) => {
    const res = await client.delete<ResultResponseData<boolean>>(
      `/api/v1/providedfile/${providedFileId}`,
      {data: {reason}},
    );
    return res.data;
  };